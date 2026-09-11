package com.greeshma.portfolio.validation.api;
import java.util.List;
public record ValidationResponse(
    String batchId,
    String source,
    int totalRecords,
    int invalidRecords,
    String status,
    List<ValidationIssue> issues
) {}
