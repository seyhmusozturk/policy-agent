package com.seyhmus.ai.policyagent.llm;

import com.seyhmus.ai.policyagent.llm.model.Conversation;
import com.seyhmus.ai.policyagent.llm.model.LlmResponse;

public interface LlmClient {
    LlmResponse chat(Conversation conversation);
}