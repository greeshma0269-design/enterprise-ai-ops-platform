package com.greeshma.portfolio.validation.api;
public record ValidationIssue(
    String recordId,
    String ruleCode,
    String severity,
    String field,
    String message
) {}
