package com.renault.garage.kafka;

import com.renault.garage.dto.response.VehicleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleEventPublisher {

    private final KafkaTemplate<String, VehicleResponseDTO> kafkaTemplate;
    private static final String TOPIC = "vehicle-events";

    public void publishVehicleCreated(VehicleResponseDTO vehicleResponse) {
        kafkaTemplate.send(TOPIC, vehicleResponse);
        System.out.println("Vehicle event published to Kafka: " + vehicleResponse);
    }
}
