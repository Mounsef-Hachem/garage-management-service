package com.renault.garage.kafka;

import com.renault.garage.dto.response.VehicleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VehicleEventConsumer {

    @KafkaListener(topics = "vehicle-events", groupId = "garage-service-group")
    public void consumeVehicleEvent(VehicleResponseDTO event) {
        log.info("Vehicle event published to Kafka topic '{}': {}", "vehicle-events", event);

        // Here we can add business logic: e.g.: record an audit, notify an external service, etc.
    }
}
