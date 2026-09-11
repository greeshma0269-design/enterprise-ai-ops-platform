package com.greeshma.portfolio.validation.service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class BatchEventPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    public BatchEventPublisher(KafkaTemplate<String,Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publish(String batchId, int invalidRecords) {
        kafkaTemplate.send("operations.events", batchId,
            Map.of("eventType","batch.validated","batchId",batchId,"invalidRecords",invalidRecords));
    }
}
