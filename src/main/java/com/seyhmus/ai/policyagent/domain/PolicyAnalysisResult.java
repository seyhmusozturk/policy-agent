package com.seyhmus.ai.policyagent.domain;

import java.util.List;

public class PolicyAnalysisResult {

    private int riskScore;
    private List<String> highRiskClauses;
    private boolean fraudSuspicion;
    private String summary;

    public PolicyAnalysisResult() {
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public List<String> getHighRiskClauses() {
        return highRiskClauses;
    }

    public void setHighRiskClauses(List<String> highRiskClauses) {
        this.highRiskClauses = highRiskClauses;
    }

    public boolean isFraudSuspicion() {
        return fraudSuspicion;
    }

    public void setFraudSuspicion(boolean fraudSuspicion) {
        this.fraudSuspicion = fraudSuspicion;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}