package sg.edu.nus.car_rental_chatbot.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.car_rental_chatbot.model.ChatRequest;
import sg.edu.nus.car_rental_chatbot.model.ChatResponse;

@Service
public class ChatService {

    @Value("${ai.local.url}")
    private String fastApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getReply(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Please enter a message.";
        }

        if (userMessage.length() > 500) {
            return "Your message is too long. Please keep it under 500 characters.";
        }

        return callFastApi(userMessage);
    }

    // Spring Boot calls FastAPI through HTTP POST.
    // Request flow: Spring sends ChatRequest JSON to Python.
    // Response flow: Python sends ChatResponse JSON back to Spring.
    private String callFastApi(String userMessage) {
        try {
            ChatRequest fastApiRequest = new ChatRequest(userMessage);
            String requestJson = objectMapper.writeValueAsString(fastApiRequest);

            HttpURLConnection connection = (HttpURLConnection) URI.create(fastApiUrl)
                    .toURL()
                    .openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Write the JSON request body that FastAPI expects:
            // {"message":"the user's message"}
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(requestJson.getBytes(StandardCharsets.UTF_8));
            }

            int statusCode = connection.getResponseCode();
            InputStream responseStream = statusCode >= 200 && statusCode < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            String responseJson = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);

            if (statusCode < 200 || statusCode >= 300) {
                return "FastAPI returned HTTP " + statusCode + ": " + responseJson;
            }

            ChatResponse chatResponse = objectMapper.readValue(responseJson, ChatResponse.class);
            return chatResponse.getReply();

        } catch (Exception error) {
            return "Could not call FastAPI service: " + error.getMessage();
        }
    }
}
