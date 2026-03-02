package com.seyhmus.ai.policyagent.agent.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyhmus.ai.policyagent.domain.PolicyAnalysisResult;
import com.seyhmus.ai.policyagent.llm.LlmClient;
import com.seyhmus.ai.policyagent.llm.model.Conversation;
import com.seyhmus.ai.policyagent.llm.model.LlmResponse;
import org.springframework.stereotype.Component;

@Component
public class PolicyAnalysisAgent {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    public PolicyAnalysisAgent(LlmClient llmClient,
                               ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
    }

    public PolicyAnalysisResult analyze(String policyText) {

        String systemPrompt =
                """
                You are an insurance policy analysis engine.

                You MUST return ONLY valid JSON.
                Do NOT add explanations.
                Do NOT add markdown.
                Do NOT wrap in ```json.
                Do NOT write anything before or after JSON.

                Required JSON structure:

                {
                  "riskScore": number,
                  "highRiskClauses": string[],
                  "fraudSuspicion": boolean,
                  "summary": string
                }

                Analyze the policy text carefully and fill all fields.
                """;

        Conversation conversation = Conversation.start(systemPrompt, policyText);

        LlmResponse response = llmClient.chat(conversation);

        String content = response.getContent();

        if (content == null || content.isBlank()) {
            throw new IllegalStateException("LLM returned empty content.");
        }

        content = content.trim();

        int start = content.indexOf("{");
        int end = content.lastIndexOf("}");

        if (start != -1 && end != -1 && end > start) {
            content = content.substring(start, end + 1);
        }

        try {
            return objectMapper.readValue(content, PolicyAnalysisResult.class);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Parsing error: LLM did not return valid JSON.\nContent:\n" + content,
                    e
            );
        }
    }
}