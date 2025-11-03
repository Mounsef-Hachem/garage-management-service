package com.renault.garage.kafka;

import com.renault.garage.dto.response.VehicleResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VehicleEventConsumer {

    @KafkaListener(topics = "vehicle-events", groupId = "garage-service-group")
    public void consumeVehicleEvent(VehicleResponseDTO event) {
        System.out.println("Vehicle event consumed: " + event);
        // Here we can add business logic: e.g.: record an audit, notify an external service, etc.
    }
}
