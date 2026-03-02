package com.seyhmus.ai.policyagent.agent.tool;

import java.util.Map;

public interface AgentTool {
    String getName();
    String getDescription();
    Map<String, Object> execute(Map<String, Object> input);


    Map<String, Object> getJsonSchema();
}