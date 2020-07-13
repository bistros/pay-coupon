package com.bistros.pay.coupon.cpservice.api;

import static com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase.CancellationRequest;
import static com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase.CancellationResponse;

import com.bistros.pay.coupon.cpservice.api.viewmodel.AllocatedCouponViewModel;
import com.bistros.pay.coupon.cpservice.api.viewmodel.CouponApprovalViewModel;
import com.bistros.pay.coupon.cpservice.api.viewmodel.CouponCancellrationViewModel;
import com.bistros.pay.coupon.cpservice.api.viewmodel.DeprecatedCouponListViewModel;
import com.bistros.pay.coupon.cpservice.api.viewmodel.MyCouponListViewModel;
import com.bistros.pay.coupon.cpservice.application.shared.EmptyRequestUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.AllocateCouponUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase.CouponApprovalRequest;
import com.bistros.pay.coupon.cpservice.application.usecase.CopuonApprovalUseCase.CouponApprovalResponse;
import com.bistros.pay.coupon.cpservice.application.usecase.CouponCancellationUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.GetAssignedCouponUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.GetAssignedCouponUseCase.GetAssignedCouponsRequest;
import com.bistros.pay.coupon.cpservice.application.usecase.GetAssignedCouponUseCase.GetAssignedCouponsResponse;
import com.bistros.pay.coupon.cpservice.application.usecase.GetDeprecatedCouponUseCase;
import com.bistros.pay.coupon.cpservice.application.usecase.GetDeprecatedCouponUseCase.GetDeprecatedCouponListUseCaseResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class CouponV1Controller {

    private final AllocateCouponUseCase allocateCouponUseCase;
    private final CopuonApprovalUseCase copuonApprovalUseCase;
    private final CouponCancellationUseCase cancellationUseCase;
    private final GetDeprecatedCouponUseCase getDeprecatedCouponUseCase;
    private final GetAssignedCouponUseCase assignedCouponUseCase;

    @GetMapping("/allocate")
    public AllocatedCouponViewModel allocateCoupon(@RequestParam("userId") String userId) {
        AllocateCouponUseCase.AssginCouponResponse response = allocateCouponUseCase.apply(new AllocateCouponUseCase.AssignCouponRequest(userId));
        return AllocatedCouponViewModel.from(response);
    }

    @GetMapping("/mycoupon")
    public MyCouponListViewModel listAssignedCoupons(@RequestParam("userId") String userId) {
        GetAssignedCouponsResponse response = assignedCouponUseCase.apply(new GetAssignedCouponsRequest(userId));
        return MyCouponListViewModel.from(response);
    }

    @GetMapping("/approval/{id}")
    public CouponApprovalViewModel approvalCoupon(@PathVariable("id") String id, @RequestParam("userId") String userId) {
        CouponApprovalResponse response = copuonApprovalUseCase.apply(new CouponApprovalRequest(id, userId));
        return CouponApprovalViewModel.from(response);
    }

    @GetMapping("/cancel/{id}")
    public CouponCancellrationViewModel cancelCoupon(@PathVariable("id") String id, @RequestParam("userId") String userId) {
        CancellationResponse response = cancellationUseCase.apply(CancellationRequest.builder().couponId(id).requestUserId(userId).build());
        return CouponCancellrationViewModel.from(response);
    }

    @GetMapping("/deprecatedList")
    public DeprecatedCouponListViewModel getDeprecatedCoupon() {
        GetDeprecatedCouponListUseCaseResponse response = getDeprecatedCouponUseCase.apply(EmptyRequestUseCase.EMPTY);
        return DeprecatedCouponListViewModel.from(response);
    }


}
