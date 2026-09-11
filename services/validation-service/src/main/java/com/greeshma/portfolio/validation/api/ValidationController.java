package com.greeshma.portfolio.validation.api;

import com.greeshma.portfolio.validation.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/validation")
@CrossOrigin(origins="*")
public class ValidationController {
    private final RecordValidator validator;
    private final BatchEventPublisher publisher;

    public ValidationController(RecordValidator validator, BatchEventPublisher publisher) {
        this.validator = validator;
        this.publisher = publisher;
    }

    @PostMapping("/batches")
    public ValidationResponse validate(@Valid @RequestBody ValidationRequest request) {
        var issues = validator.validate(request.records());
        long invalid = issues.stream().filter(i -> "ERROR".equals(i.severity()))
            .map(ValidationIssue::recordId).distinct().count();
        String batchId = UUID.randomUUID().toString();
        publisher.publish(batchId, (int) invalid);

        return new ValidationResponse(
            batchId,
            request.source(),
            request.records().size(),
            (int) invalid,
            issues.isEmpty() ? "COMPLETED" : "COMPLETED_WITH_EXCEPTIONS",
            issues
        );
    }
}
