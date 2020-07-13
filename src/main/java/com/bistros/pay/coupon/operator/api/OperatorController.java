package com.bistros.pay.coupon.operator.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.bistros.pay.coupon.operator.api.viewmodel.RandomGenerateViewModel;
import com.bistros.pay.coupon.operator.application.GenCouponService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/operator")
@AllArgsConstructor
public class OperatorController {

    private final GenCouponService couponService;


    @GetMapping("/gen")
    @ResponseBody
    public RandomGenerateViewModel generateCoupon(
        @RequestParam(name = "count", defaultValue = "1") @Min(1) @Max(50000) int count) {

        LocalDateTime requestTime = LocalDateTime.now();
        int created = couponService.create(count);

        return RandomGenerateViewModel.builder()
            .requestTime(requestTime)
            .completedTime(LocalDateTime.now())
            .size(created).build();
    }

    @GetMapping("/upload")
    public String uploadPage(Model model) {
        return "upload";
    }

    @PostMapping("/upload")
    public String dumpUpload(@RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes) throws IOException {

        File tmpFile = File.createTempFile("_" + System.nanoTime(), ".tmp");
        file.transferTo(tmpFile);
        List<String> lines = Files.readAllLines(Paths.get(tmpFile.toString()));
        int size = couponService.save(lines);

        String msg = file.getOriginalFilename() + "파일을 이용해서 " + size + "건의 쿠폰이 등록되었습니다";

        redirectAttributes.addFlashAttribute("message", msg);

        return "redirect:/operator/upload";

    }

}
