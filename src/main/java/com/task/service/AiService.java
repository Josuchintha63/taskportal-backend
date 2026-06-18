package com.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.dto.AiTaskResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiTaskResponse generateTaskInfo(String title) {

        try {

            String prompt = String.format("""
                    Given the task title: "%s",
                    respond ONLY with a JSON object in this format:

                    {
                      "description":"task description",
                      "priority":"LOW",
                      "estimatedEffort":"2 hours"
                    }
                    """, title);

            Map<String, Object> requestBody = Map.of(
                    "contents",
                    new Object[]{
                            Map.of(
                                    "parts",
                                    new Object[]{
                                            Map.of("text", prompt)
                                    }
                            )
                    }
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(requestBody, headers);

            String url = apiUrl + "?key=" + apiKey;

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            url,
                            entity,
                            String.class
                    );

            return parseGeminiResponse(response.getBody());

        } catch (Exception e) {

            e.printStackTrace();

            return new AiTaskResponse(
                    "Could not generate description automatically. Please fill manually.",
                    "MEDIUM",
                    "1 hour"
            );
        }
    }

    private AiTaskResponse parseGeminiResponse(String rawJson) throws Exception {

        JsonNode root = objectMapper.readTree(rawJson);

        String text = root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        String cleaned = text
                .replace("```json", "")
                .replace("```", "")
                .trim();

        JsonNode parsed = objectMapper.readTree(cleaned);

        return new AiTaskResponse(
                parsed.path("description")
                        .asText("No description generated"),

                parsed.path("priority")
                        .asText("MEDIUM"),

                parsed.path("estimatedEffort")
                        .asText("1 hour")
        );
    }
}