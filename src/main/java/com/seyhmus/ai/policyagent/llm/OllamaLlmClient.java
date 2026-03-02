package com.seyhmus.ai.policyagent.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyhmus.ai.policyagent.llm.model.Conversation;
import com.seyhmus.ai.policyagent.llm.model.LlmResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.Proxy;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OllamaLlmClient implements LlmClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String model;
    private final String baseUrl;

    public OllamaLlmClient(
            ObjectMapper objectMapper,
            @Value("${ollama.model:llama3}") String model,
            @Value("${ollama.base-url:http://127.0.0.1:11434}") String baseUrl
    ) {
        this.objectMapper = objectMapper;
        this.model = model;
        this.baseUrl = baseUrl;

        this.client = new OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public LlmResponse chat(Conversation conversation) {
        String prompt = buildPrompt(conversation);

        Map<String, Object> body = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false
        );

        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(body);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Ollama request body", e);
        }

        Request request = new Request.Builder()
                .url(baseUrl + "/api/generate")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(jsonBody, JSON))
                .build();

        for (int attempt = 1; attempt <= 2; attempt++) {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String err = response.body() != null ? response.body().string() : "";
                    throw new RuntimeException("Ollama error HTTP " + response.code() + " body=" + err);
                }

                ResponseBody rb = response.body();
                if (rb == null) throw new RuntimeException("Ollama returned empty body");

                String raw = rb.string();

                JsonNode root = objectMapper.readTree(raw);
                String content = root.path("response").asText(null);

                if (content == null) {
                    throw new RuntimeException("Invalid Ollama response format: " + raw);
                }

                return LlmResponse.builder()
                        .toolCall(false)
                        .content(content)
                        .build();

            } catch (SocketException se) {
                if (attempt == 2) throw new RuntimeException("Ollama socket closed (after retry)", se);
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            } catch (Exception e) {
                throw new RuntimeException("Ollama call failed", e);
            }
        }

        throw new IllegalStateException("Unreachable");
    }

    private String buildPrompt(Conversation conversation) {
        StringBuilder sb = new StringBuilder();
        conversation.getMessages().forEach(m -> sb.append(m.getContent()).append('\n'));
        return sb.toString();
    }
}