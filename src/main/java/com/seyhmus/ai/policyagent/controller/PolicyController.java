package com.seyhmus.ai.policyagent.controller;

import com.seyhmus.ai.policyagent.agent.engine.PolicyAnalysisAgent;
import com.seyhmus.ai.policyagent.domain.PolicyAnalysisResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyAnalysisAgent agent;

    public PolicyController(PolicyAnalysisAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/analyze")
    public PolicyAnalysisResult analyze(@RequestBody String policyText) {
        return agent.analyze(policyText);
    }
}