package com.renault.garage.integration;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.model.Garage;
import com.renault.garage.model.Vehicle;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = { "vehicle-created" })
class VehicleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Garage garage;

    @BeforeEach
    void setup() {
        garage = new Garage();
        garage.setName("Test Garage");
        garage.setAddress("123 Test Street");
        garage.setTelephone("1234567890");
        garage.setEmail("test@garage.com");
        garage = garageRepository.save(garage);
    }

    @Test
    void createVehicle_integration_success() throws Exception {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO("Renault", 2020, "Diesel");

        mockMvc.perform(post("/api/vehicles/{garageId}", garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.manufacturingYear").value(2020));

        // Verify the vehicle is actually persisted
        List<Vehicle> vehicles = vehicleRepository.findAll();
        assertThat(vehicles).hasSize(1);
        assertThat(vehicles.getFirst().getBrand()).isEqualTo("Renault");
    }
}
