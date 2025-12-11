package com.careservice.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class VNPayDebugUtil {
    
    public static String debugSignatureGeneration(Map<String, String> params, String hashSecret) {
        log.info("=== VNPay Signature Debug ===");
        log.info("Parameters count: {}", params.size());
        
        // Create sorted copy
        Map<String, String> sortedParams = new TreeMap<>(params);
        sortedParams.remove("vnp_SecureHash");
        sortedParams.remove("vnp_SecureHashType");
        
        log.info("Sorted parameters:");
        sortedParams.forEach((k, v) -> log.info("  {} = {}", k, v));
        
        // Build query string
        StringBuilder queryBuilder = new StringBuilder();
        sortedParams.forEach((key, value) -> {
            if (queryBuilder.length() > 0) {
                queryBuilder.append("&");
            }
            try {
                queryBuilder.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("Encoding error", e);
            }
        });
        
        String queryString = queryBuilder.toString();
        log.info("Query String: {}", queryString.substring(0, Math.min(200, queryString.length())) + "...");
        log.info("Hash Secret: {}", hashSecret);
        log.info("Hash Secret Length: {}", hashSecret.length());
        
        try {
            String signature = hmacSHA512(hashSecret, queryString);
            log.info("Generated Signature: {}", signature);
            return signature;
        } catch (Exception e) {
            log.error("Signature generation failed", e);
            return null;
        }
    }
    
    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Error computing HMAC SHA512", e);
            throw new RuntimeException(e);
        }
    }
}
