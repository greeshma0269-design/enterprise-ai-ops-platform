package com.greeshma.portfolio.reconciliation.api;
public record ReconciliationIssue(
    String recordId,String field,String issueType,String sourceValue,String targetValue
) {}
