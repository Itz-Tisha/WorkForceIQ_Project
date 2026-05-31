package com.example.WorkforceIQ.Service;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AIAnalysisService {

    @Value("${groq.api.key}")
    private String apiKey;

    public String getAnalysis(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        String url =
            "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();

        body.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> msg = new HashMap<>();

        msg.put("role", "user");

        msg.put("content", prompt);

        messages.add(msg);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> entity =
            new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
            );

        List choices =
            (List) response.getBody().get("choices");

        Map choice = (Map) choices.get(0);

        Map message =
            (Map) choice.get("message");

        return message.get("content").toString();
    }
}