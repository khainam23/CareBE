package com.careservice.service;

import com.careservice.dto.ekyc.EkycResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FptAiService {

    @Value("${API_FPT_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FPT_ID_CARD_URL = "https://api.fpt.ai/vision/idr/vnm";

    public EkycResponse extractIdCard(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("api-key", apiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(FPT_ID_CARD_URL, requestEntity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to call FPT.AI API: " + response.getStatusCode());
            }

            return parseFptResponse(response.getBody());
        } catch (Exception e) {
            log.error("Error calling FPT.AI API", e);
            throw new RuntimeException("Error processing ID card: " + e.getMessage());
        }
    }

    private EkycResponse parseFptResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            int errorCode = root.path("errorCode").asInt();
            
            if (errorCode != 0) {
                String errorMessage = root.path("errorMessage").asText();
                throw new RuntimeException("FPT.AI Error: " + errorMessage);
            }

            JsonNode data = root.path("data").get(0);
            if (data == null) {
                throw new RuntimeException("No data found in FPT.AI response");
            }

            return EkycResponse.builder()
                    .idNumber(data.path("id").asText())
                    .name(data.path("name").asText())
                    .dob(data.path("dob").asText())
                    .sex(data.path("sex").asText())
                    .nationality(data.path("nationality").asText())
                    .homeAddress(data.path("home").asText())
                    .residenceAddress(data.path("address").asText())
                    .expiryDate(data.path("doe").asText())
                    .build();

        } catch (Exception e) {
            log.error("Error parsing FPT.AI response", e);
            throw new RuntimeException("Error parsing ID card data");
        }
    }
}
