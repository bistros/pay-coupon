## 1. 랜덤 쿠폰 N 개 생성
```
   curl -XGET http://localhost:8080/operator/gen?count=10
   10개의 쿠폰을 생성하여 데이터베이스에 저장 
```
   * operator 서비스에서 랜덤문자를 가지는 쿠폰을 생성한다. 
   쿠폰은 16자리 (4-4-4-4)로 구성되어 있고 Function 을 상속한 CouponPrefixGenerator, DashAppender, ValidCodeGenerator 를 체이닝하여 생성된다.

      - 1번 자리 : 쿠폰발급기의 구분자
      - 2번 ~ 14번자리 : 0-9A-Z 를 활용하여 랜덤 문자
      - 15번 ~ 16번자리 : 앞의 14자리를 가지고 유효한 쿠폰인지를 체크하는 Validation 문자 2개

      이렇게 총 16자리 구성된다.
   

   * 쿠폰을 생성하는 것은 ConcurrencyCouponGenerator 클래스가 여러개의 Worker Instance 를 가지고 분할 수행한다. 사실 `range(0,10000).parallel()` 하는 것에 비해 성능상의 장점은 없지만, 추후에 이 로직이 복잡해져서 부하가 심해지거나, 분산환경으로 옮겨야 할 때등에도 같은 개념을 사용 할 수 있기 때문에 적용하였다.
      

## 2.사용자에게 쿠폰 1개 발급 
```
   curl -XGET http://localhost:8080/v1/allocate?userId=TK
   사용자 TK 에서 쿠폰 1개를 발급 함

   Exception
      CouponOutOfStockException : 발급 가능한 쿠폰이 없음
```

  * 문제에 따르면 요청이 올 때 쿠폰을 생성하는 것이 아닌 '쿠폰을 생성해놓고 사용자를 할당'하는 방식이다. 이러면 순간적인 쿠폰 발급 요청시 '발급'에 따른 부하는 없지만 '쿠폰을 할당하는 것에 대한 고민'이 필요하다.

  미사용 쿠폰에서 하나의 쿠폰을 할당 받기 위해서는 `UPDATE * SET OWNER='ID' WHERE (select * from WHERE STATE = UNASSIGN limit 1) `
   이처럼 '미사용 쿠폰을 가져온다 -> UPDATE 한다' 처럼 2 Phase 가 필요로 하고, 중복 할당을 막기 위해 트랜잭션등을 사용해야 한다.  이 보다는 '할당'을 위해서 Queue 를 사용하는 것을 제안했다.

  미사용 쿠폰을 큐에 올려 놓고 -> 할당 하고 -> 정보를 DB에 동기화한다.

  RabbitMQ , Kafka 등은 10K 문제를 쉽게 해결 할 수 있는 솔루션이라서 서비스 레벨에서 구현 가능 여부는 고민할 필요 없이 추후 어느 것을 사용할지만 고르면 된다.

## 3.사용자의 쿠폰 목록 확인 
```
   curl -XGET http://localhost:8080/v1/mycoupon?userId=TK
   사용자 TK 에게 할당된 쿠폰 목록을 보여줌
```
   

## 4. 쿠폰 사용 요청
```
   curl -XGET http://localhost:8080/v1/approval/1234-5678-1234-5678?userId=TK
   TK 에게 할당이 되어 있는 쿠폰을 '사용처리(USED)' 함

   Exception
      FailedUseCouponException   : 이미 사용된 쿠폰
      NotExistCouponException    : 존재 하지 않는 쿠폰
```
1. 예외처리
    - 이미 사용된 쿠폰
    - 존재하지 않는 쿠폰
    - 쿠폰은 존재하지만 쿠폰 사용 요청자랑, 쿠폰 소유자랑 다른 경우
      
      해당 예외 케이스는 '다른 사람의 쿠폰 사용자' 라고 표시할 수도 있지만 단순히 '사용자가 사용할 수 있는 쿠폰이 아니라는 표현이 더 적절한듯하여 NotExist 로 처리함'

## 5. 쿠폰 취소
```
   curl -XGET http://localhost:8080/v1/cancel/1234-5678-1234-5678?userId=TK
   TK 에게 할당된 쿠폰을 취소함

   Exception
      AlreadyUsedCouponException : 이미 사용된 쿠폰
      NotExistCouponException : 존재 하지 않는 쿠폰 
```
   
1. 취소 상태인 쿠폰을 '사용 가능' 상태로 복구 하는 방법은 없다

1. 더 좋은 방법은 없는가? 
   UPDATE SET State = '취소' where ID = "쿠폰ㅑ" 를 먼저 수행하여 update row count 가 1이면 성공적으로 취소
   아니라면 다시 한번 select 해서 이미 사용(USED), 취소(CANCEL)인지를 확인 하는 방법도 있다.
   
   이러한 방법은 '취소 API'가 호출이 될 때 대부분 성공한다는 '낙관적인 결과'를 기대할 수 있을 경우 효율적이므로 
   적당히 운영하다가 로직을 변경하는 것을 고려할 수 있다.
   
1. 일반적으로 대량의 쿠폰이 취소 될 경우는 '특정 이벤트'를 위해 잘 못 발급된 경우이지만 제안서에서는 그러한 내용이 없다
   
   그리하여 쿠폰 취소는 요청이 들어올 때마다 Database 에 '취소 상태'로 업데이트를 하는 동기 API로 간단히 작성되었다.
     

## 6. 만기 쿠폰 전체 보기
   ```
      curl -XGET http://localhost:8080/v1/deprecatedList

   ```

   * 기능  : 당일 기준으로 사용 기간이 어제인 쿠폰들의 목록을 보여준다. 

      예를 들어 오늘이 7월 11일 이라고 할 경우 'EXPIRATION_DATE' 가 2020-07-10 이고 사용자가 정상적으로 사용가능한 쿠폰의 목록을 반환한다.

## 그 외 기능

### 파일 업로드
   import 폴더에 file1.dump 파일에는 '10만개'의 임시 쿠폰 번호가 들어 있다.
   `http://localhost:8080/operator/upload ` 를 이용해서 업로드 해보면 3초 정도가 걸리고 성공한다.
   (파일 로드나, 파싱이 문제가 아닌 H2DB 에 저장하는 부분이 느려서 오래 걸린다)
### 만료기간 쿠폰을 체크후 콘솔 출력
   Scheduler 를 이용해 매일 오전 9시 '만료 쿠폰 체크 이벤트(RequestCheckExpireDateEvent)를 발행한다'
   이 메시지를 다른 서비스 클래스가 수신해서 유효 기간이 3일 남은 쿠폰들을 콘솔에 출력한다.

   Message Driven 방식으로 구현하여, 추후 분산 환경에서 좀 더 안정적인 운영이 가능하도록 했다. 
   
   그리고 이러한 Push 들은 새벽/밤 에는 거부감이 들 수 있으니, 일과중에 보내는 것이 좋다.

### H2 Console
   H2-Memory DB를 사용하고 있고, 현재 상황을 눈으로 쉽게 보도록 H2 UI 를 오픈하였다.
   `http://localhost:8080/h2/` 으로 연결하여 그대로 Connect 하면 'COUPON' DB를 확인 할 수 있다.   