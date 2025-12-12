package com.careservice.controller;

import com.careservice.dto.ApiResponse;
import com.careservice.dto.ekyc.EkycResponse;
import com.careservice.service.FptAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth/ekyc")
@RequiredArgsConstructor
public class EkycController {

    private final FptAiService fptAiService;

    @PostMapping("/extract-id")
    public ResponseEntity<ApiResponse<EkycResponse>> extractIdCard(@RequestParam("image") MultipartFile image) {
        try {
            EkycResponse response = fptAiService.extractIdCard(image);
            return ResponseEntity.ok(ApiResponse.success("Extracted ID card data successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
