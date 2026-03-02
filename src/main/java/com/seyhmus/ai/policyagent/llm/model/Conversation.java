package com.seyhmus.ai.policyagent.llm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Conversation {

    private final List<Message> messages = new ArrayList<>();

    public static Conversation start(String systemPrompt, String userPrompt) {
        Conversation c = new Conversation();
        c.addSystem(systemPrompt);
        c.addUser(userPrompt);
        return c;
    }

    public void addSystem(String content) {
        messages.add(Message.system(content));
    }

    public void addUser(String content) {
        messages.add(Message.user(content));
    }

    public void addAssistant(String content) {
        messages.add(Message.assistant(content));
    }

    public void addToolResult(String toolCallId, String toolName, String resultJson) {
        messages.add(Message.tool(toolCallId, toolName, resultJson));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Map<String, Object>> toOpenAiMessages() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (Message m : messages) out.add(m.toOpenAiMap());
        return out;
    }
}