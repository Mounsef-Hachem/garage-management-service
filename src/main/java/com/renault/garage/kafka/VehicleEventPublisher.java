package com.renault.garage.kafka;

import com.renault.garage.dto.response.VehicleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleEventPublisher {

    private final KafkaTemplate<String, VehicleResponseDTO> kafkaTemplate;
    private static final String TOPIC = "vehicle-events";

    public void publishVehicleCreated(VehicleResponseDTO vehicleResponse) {
        kafkaTemplate.send(TOPIC, vehicleResponse);
        log.info("Vehicle event published to Kafka topic '{}': {}", TOPIC, vehicleResponse);

    }
}
