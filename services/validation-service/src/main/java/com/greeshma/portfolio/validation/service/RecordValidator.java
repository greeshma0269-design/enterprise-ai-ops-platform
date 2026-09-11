package com.greeshma.portfolio.validation.service;

import com.greeshma.portfolio.validation.api.*;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;

@Component
public class RecordValidator {
    public List<ValidationIssue> validate(List<OperationRecord> records) {
        List<ValidationIssue> issues = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (OperationRecord r : records) {
            if (!seen.add(r.recordId())) {
                issues.add(new ValidationIssue(r.recordId(),"DUPLICATE_RECORD","ERROR","recordId","Duplicate recordId found."));
            }
            if (r.price().compareTo(BigDecimal.ZERO) <= 0) {
                issues.add(new ValidationIssue(r.recordId(),"NON_POSITIVE_PRICE","ERROR","price","Price must be greater than zero."));
            }
        }
        return issues;
    }
}
