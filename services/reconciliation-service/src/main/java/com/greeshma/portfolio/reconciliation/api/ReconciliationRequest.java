package com.greeshma.portfolio.reconciliation.api;
import java.util.List;
public record ReconciliationRequest(List<RecordData> sourceRecords, List<RecordData> targetRecords) {}
