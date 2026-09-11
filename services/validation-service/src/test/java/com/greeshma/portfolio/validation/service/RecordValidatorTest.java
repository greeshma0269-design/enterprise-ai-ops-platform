package com.greeshma.portfolio.validation.service;
import com.greeshma.portfolio.validation.api.OperationRecord;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class RecordValidatorTest {
    @Test
    void rejectsInvalidPrice() {
        var r = new OperationRecord("1","SKU-1","CA",LocalDate.now(),new BigDecimal("-1"),"ACTIVE","demo");
        assertThat(new RecordValidator().validate(List.of(r)))
            .anyMatch(i -> i.ruleCode().equals("NON_POSITIVE_PRICE"));
    }
}
