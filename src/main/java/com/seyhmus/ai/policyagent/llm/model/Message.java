package com.seyhmus.ai.policyagent.llm.model;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private final String role;
    private final String content;

    private final String toolCallId;
    private final String name;

    private Message(String role, String content, String toolCallId, String name) {
        this.role = role;
        this.content = content;
        this.toolCallId = toolCallId;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public static Message system(String content) { return new Message("system", content, null, null); }
    public static Message user(String content) { return new Message("user", content, null, null); }
    public static Message assistant(String content) { return new Message("assistant", content, null, null); }

    public static Message tool(String toolCallId, String name, String content) {
        return new Message("tool", content, toolCallId, name);
    }

    public Map<String, Object> toOpenAiMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);

        if ("tool".equals(role)) {
            m.put("tool_call_id", toolCallId);
            m.put("name", name);
        }
        return m;
    }
}