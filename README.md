

# 개발 관련 및 프로젝트

## 개발 환경
    - Language : Java 1.8 
    - Framework
        Spring Boot 2.3.1 , Spring Framework 5.2.7
        Spring Data Jdbc 2.0.1
    - Develop Env
        EditorConfig
        Gradle v6.4

## 실행 방법
* Build & Test
    ``` 
    ./gradlew clean build
    ``` 
* Run
   ```
    java -jar build/libs/coupon-0.0.1-SNAPSHOT.jar
   ```

## 프로젝트 구조
 * 하나의 git repo 이지만, 내부적으로 service, operator 2개의 서비스로 생각하고 만들었다.
   * cpservice - 쿠폰 할당, 쿠폰 사용, 쿠폰 취소, 쿠폰 조회등 서비스 기능을 구현
      * RDB skill set : 'spring-data-jdbc'
   * operator - 쿠폰 생성, CSV 업로드, 만료기한 공지등 운영성 기능을 구현 
      * 구현 기능
         * 쿠폰 생성 :  /operator/gen
         * CSV Upload : /operator/upload
      * RDB skill set  : 'spring-jdbc' && 'spring-data-jdbc'
   * [API 문서](./API.md) 
      



----
   
# 제약사항
* 인증 구현 : 구현 안함 

* 100억개 이상 쿠폰 관리
  * 쿠폰 패턴 : 0-9A-Z 총 30여개의 문자로 16자리를 생성하기 때문에 충분한 가용 공간을 가지고 있다
  * 생성 : 단일 머신에서 몇 십만개씩 생성하는 것보다는 다중 머신에서 생성 할 수 있도록 구현 함 (API 문서 참고)
   
* CSV import
  * `/operator/upload` UI로 테스트 가능

* 대용량 트래픽 
  * 대규모의 데이터를 RDB(문제 요건)에서 처리하기 위해서는 정규화는 RDB에 부하를 주는 좋은 방법은 아니다.
     이를 해결 하기 위한 방법으로는 반정규화(데이터의 중복 적재) 와 RDB의 샤딩을 고려할 수 있다.
     특히 '쿠폰' 자체는 독립 수행이 가능한 객체이기 때문에 '샤딩'의 효과를 충분히 누릴 수 있다.
     쿠폰키 자체를 샤드키로 쓸 경우, 'my coupon'을 조회하는 쪽에서는 성능이 떨어질 수 있지만 이 서비스의 가장 큰 목적은 '빠르게 쿠폰을 발급'하는 것에 더 초점을 둔다.
     
  * 또는 Message Driven 을 통한 RDB 의 부하 분산을 생각할 수 있다.
     데이터를 생성할 때 RDB, Queue(RabbitMQ, Kafka) 등에 동시에 적재하고, 쿠폰을 발급할 때에도 Queue 에서 쿠폰 번호를 가져온다.
     그리고 발급 정보를 RDB 에 저장하는 방식으로 구현한다. 


# 고민한 내용
 * 쿠폰 발급은 단일 머신으로 진행 할 것인가?
   * 솔직히 100만개를 만들더라도 '쿠폰'을 생성하는 것 자체는 많은 시간이 걸리지 않았다. (시간을 잡아 먹는 요소는 H2에 저장하는 부분이었다)
     그렇기 때문에 '발급기'를 Multiple Worker로 하는것에 대한 효융성은 없지만, 이러한 방식으로 쿠폰의 발급, 할당등을 할 수 있다는 걸 보여주기 위해서 가장 단순하게 구현 함
 * 쿠폰의 할당은 ?
   * 단순한 방법은 '미달할(UNASSIGN)' 쿠폰을 1개 가져와서 사용자에게 할당을 하는 것이지만. 이럴 경우 성능상의 문제가 발생한다.
     `트랜잭션 열고 - Select 쿠폰 - Update 쿠폰 - 트랜잭션 닫고` 의 순서가 되기 때문에 10K 등의 이슈에서 큰 장애가 된다.
     
       
    
