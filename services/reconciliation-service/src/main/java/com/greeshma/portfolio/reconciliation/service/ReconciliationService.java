package com.greeshma.portfolio.reconciliation.service;
import com.greeshma.portfolio.reconciliation.api.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ReconciliationService {
    public List<ReconciliationIssue> reconcile(ReconciliationRequest request) {
        Map<String,RecordData> target = new HashMap<>();
        request.targetRecords().forEach(r -> target.put(r.recordId(), r));
        List<ReconciliationIssue> issues = new ArrayList<>();

        for (var source : request.sourceRecords()) {
            var other = target.get(source.recordId());
            if (other == null) {
                issues.add(new ReconciliationIssue(source.recordId(),"record","MISSING_TARGET_RECORD",source.recordId(),null));
                continue;
            }
            if (source.price().compareTo(other.price()) != 0) {
                issues.add(new ReconciliationIssue(source.recordId(),"price","VALUE_MISMATCH",source.price().toString(),other.price().toString()));
            }
            if (!Objects.equals(source.status(), other.status())) {
                issues.add(new ReconciliationIssue(source.recordId(),"status","VALUE_MISMATCH",source.status(),other.status()));
            }
        }
        return issues;
    }
}
