package vn.edu.hcmute.utecare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.edu.hcmute.utecare.service.GeminiService;

@Service
public class GeminiServiceImpl implements GeminiService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "AIzaSyDvX5M3sGXPn3-lO9xFil5vps_mXACFXUA";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @Override
    public String callGeminiAPI(String prompt) {

        String jsonBody = String.format("{ \"contents\": [ { \"parts\": [ { \"text\": \"%s\" } ] } ] }", prompt + "Viết ngắn gọn");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode candidatesNode = rootNode.path("candidates");
                if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                    JsonNode contentNode = candidatesNode.get(0).path("content");
                    JsonNode partsNode = contentNode.path("parts");
                    if (partsNode.isArray() && partsNode.size() > 0) {
                        String responseText = partsNode.get(0).path("text").asText();
                        return responseText;  // Return the text from the API
                    }
                }
                return "No response text found!";
            } catch (Exception e) {
                return "Error processing response: " + e.getMessage();
            }
        } else {
            return "Error: " + response.getStatusCode() + " - " + response.getBody();
        }
    }
}