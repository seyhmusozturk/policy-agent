package com.seyhmus.ai.policyagent.llm.model;

import java.util.Map;

public class LlmResponse {

    private final String content;

    private final boolean toolCall;
    private final String toolCallId;
    private final String toolName;
    private final Map<String, Object> toolArguments;

    private LlmResponse(Builder builder) {
        this.content = builder.content;
        this.toolCall = builder.toolCall;
        this.toolCallId = builder.toolCallId;
        this.toolName = builder.toolName;
        this.toolArguments = builder.toolArguments;
    }

    public String getContent() {
        return content;
    }

    public boolean isToolCall() {
        return toolCall;
    }

    public String getToolCallId() {       // ✅ eklendi
        return toolCallId;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getToolArguments() {
        return toolArguments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String content;

        private boolean toolCall;
        private String toolCallId;
        private String toolName;
        private Map<String, Object> toolArguments;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder toolCall(boolean toolCall) {
            this.toolCall = toolCall;
            return this;
        }

        public Builder toolCallId(String toolCallId) {
            this.toolCallId = toolCallId;
            return this;
        }

        public Builder toolName(String toolName) {
            this.toolName = toolName;
            return this;
        }

        public Builder toolArguments(Map<String, Object> toolArguments) {
            this.toolArguments = toolArguments;
            return this;
        }

        public LlmResponse build() {
            return new LlmResponse(this);
        }
    }
}