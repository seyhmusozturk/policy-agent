package com.seyhmus.ai.policyagent.llm.model;

public class LlmRequest {

    private final String systemPrompt;
    private final String userPrompt;

    private LlmRequest(Builder builder) {
        this.systemPrompt = builder.systemPrompt;
        this.userPrompt = builder.userPrompt;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String systemPrompt;
        private String userPrompt;

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder userPrompt(String userPrompt) {
            this.userPrompt = userPrompt;
            return this;
        }

        public LlmRequest build() {
            return new LlmRequest(this);
        }
    }
}