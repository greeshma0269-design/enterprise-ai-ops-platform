package com.greeshma.portfolio.validation.api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record ValidationRequest(
    @NotBlank String source,
    @NotEmpty List<@Valid OperationRecord> records
) {}
