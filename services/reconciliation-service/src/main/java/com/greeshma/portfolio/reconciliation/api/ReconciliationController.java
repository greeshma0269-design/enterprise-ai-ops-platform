package com.greeshma.portfolio.reconciliation.api;
import com.greeshma.portfolio.reconciliation.service.ReconciliationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reconciliation")
@CrossOrigin(origins="*")
public class ReconciliationController {
    private final ReconciliationService service;
    public ReconciliationController(ReconciliationService service) { this.service = service; }

    @PostMapping
    public List<ReconciliationIssue> reconcile(@RequestBody ReconciliationRequest request) {
        return service.reconcile(request);
    }
}
