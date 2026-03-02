package com.seyhmus.ai.policyagent.agent.tool;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RiskScoringTool implements AgentTool {

    @Override
    public String getName() {
        return "risk_scoring_tool";
    }

    @Override
    public Map<String, Object> getJsonSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "clause", Map.of("type", "string")
                ),
                "required", java.util.List.of("clause")
        );
    }


    @Override
    public String getDescription() {
        return "Calculates risk score based on risky clauses.";
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> input) {

        String clause = (String) input.get("clause");

        int score = 10;

        if (clause != null) {
            if (clause.contains("hariç")) score += 30;
            if (clause.contains("%25")) score += 20;
        }

        return Map.of("riskScore", score);
    }
}