package com.greeshma.portfolio.validation.api;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationRecord(
    @NotBlank String recordId,
    @NotBlank String sku,
    @NotBlank String market,
    @NotNull LocalDate effectiveDate,
    @NotNull BigDecimal price,
    @NotBlank String status,
    @NotBlank String sourceSystem
) {}
