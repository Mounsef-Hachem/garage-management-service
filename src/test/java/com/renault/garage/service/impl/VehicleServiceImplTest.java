package com.renault.garage.service.impl;

import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.exception.StorageLimitExceededException;
import com.renault.garage.kafka.VehicleEventPublisher;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.model.Garage;
import com.renault.garage.model.Vehicle;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehicleEventPublisher eventPublisher;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Garage sampleGarage;
    private VehicleRequestDTO sampleRequest;
    private Vehicle sampleVehicle;
    private VehicleResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleGarage = Garage
                .builder()
                .id(1L)
                .name("Sample Garage")
                .address("123 Main St")
                .telephone("+33123456789")
                .email("test@test.te")
                .build();
        sampleGarage.setId(1L);

        sampleRequest = new VehicleRequestDTO("Renault", 2022, "Gasoline");

        sampleVehicle = Vehicle
                .builder()
                .brand("Renault")
                .fuelType("Gasoline")
                .manufacturingYear(2022)
                .build();

        sampleResponse = VehicleResponseDTO.builder()
                .id(10L)
                .brand("Renault")
                .manufacturingYear(2022)
                .fuelType("Gasoline")
                .build();
    }

    @Test
    void createVehicle_should_SaveAndReturnVehicleResponseDTO_when_GarageFoundAndValidRequestIsProvided() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(sampleGarage));
        when(vehicleRepository.countByGarage_Id(1L)).thenReturn(0L);
        when(vehicleMapper.toEntity(any(VehicleRequestDTO.class))).thenReturn(sampleVehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(sampleVehicle);
        when(vehicleMapper.toResponseDTO(any(Vehicle.class))).thenReturn(sampleResponse);

        VehicleResponseDTO result = vehicleService.createVehicle(1L, sampleRequest);

        assertThat(result).isEqualTo(sampleResponse);

        verify(garageRepository).findById(1L);
        verify(vehicleRepository).countByGarage_Id(1L);
        verify(vehicleRepository).save(sampleVehicle);
        verify(vehicleMapper).toResponseDTO(sampleVehicle);
        verify(eventPublisher).publishVehicleCreated(sampleResponse);
    }

    @Test
    void createVehicle_should_ThrowResourceNotFoundException_when_GarageNotFound() {

        when(garageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.createVehicle(99L, sampleRequest));

        verify(garageRepository).findById(99L);
        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void createVehicle_shouldThrowStorageLimitExceededException_when_QuotaExceeded() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(sampleGarage));
        when(vehicleRepository.countByGarage_Id(1L)).thenReturn(50L); // quota reached

        assertThrows(StorageLimitExceededException.class, () -> vehicleService.createVehicle(1L, sampleRequest));

        verify(garageRepository).findById(1L);
        verify(vehicleRepository).countByGarage_Id(1L);
        verify(vehicleRepository, never()).save(sampleVehicle);
    }

    @Test
    void getVehicleByGarage_should_ReturnListOfVehicleResponseDTO_when_GarageFound() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(sampleGarage));
        when(vehicleRepository.findByGarage(any(Garage.class))).thenReturn(List.of(sampleVehicle));
        when(vehicleMapper.toVehicleResponseDTOList(anyList())).thenReturn(List.of(sampleResponse));

        List<VehicleResponseDTO> result = vehicleService.getVehicleByGarage(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(sampleResponse);

        verify(garageRepository).findById(1L);
        verify(vehicleRepository).findByGarage(sampleGarage);
        verify(vehicleMapper).toVehicleResponseDTOList(List.of(sampleVehicle));
    }

    @Test
    void updateVehicle_should_ReturnVehicleResponseDTO_when_ValidRequestIsProvided() {

        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(sampleVehicle));
        doAnswer(invocation -> {
            VehicleRequestDTO dto = invocation.getArgument(0);
            Vehicle entity = invocation.getArgument(1);
            entity.setBrand(dto.brand());
            entity.setManufacturingYear(dto.manufacturingYear());
            entity.setFuelType(dto.fuelType());
            return null;
        }).when(vehicleMapper).updateVehicleFromDTO(any(VehicleRequestDTO.class), any(Vehicle.class));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(sampleVehicle);
        when(vehicleMapper.toResponseDTO(any(Vehicle.class))).thenReturn(sampleResponse);

        VehicleResponseDTO result = vehicleService.updateVehicle(10L, sampleRequest);

        assertThat(result).isEqualTo(sampleResponse);

        verify(vehicleRepository).findById(10L);
        verify(vehicleMapper).updateVehicleFromDTO(sampleRequest, sampleVehicle);
        verify(vehicleRepository).save(sampleVehicle);
        verify(vehicleMapper).toResponseDTO(sampleVehicle);
    }

    @Test
    void deleteVehicle_shouldThrowResourceNotFoundException_when_VehicleNotExist() {
        when(vehicleRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.deleteVehicle(99L));

        verify(vehicleRepository).existsById(99L);
        verify(vehicleRepository, never()).deleteById(99L);
    }

    @Test
    void getVehiclesByBrandAndGarages_WithoutGarageIds_should_ReturnListOfVehicleResponseDTO_when_ValidBrandAndWithoutGarageIds() {
        when(vehicleRepository.findByBrandContainingIgnoreCase(anyString())).thenReturn(List.of(sampleVehicle));
        when(vehicleMapper.toResponseDTO(any(Vehicle.class))).thenReturn(sampleResponse);

        List<VehicleResponseDTO> result = vehicleService.getVehiclesByBrandAndGarages("Renault", null);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(sampleResponse);

        verify(vehicleRepository).findByBrandContainingIgnoreCase("Renault");
        verify(vehicleMapper).toResponseDTO(sampleVehicle);
    }

    @Test
    void getVehiclesByBrandAndGarages_WithGarageIds_should_ReturnListOfVehicleResponseDTO_when_ValidBrandAndWithGarageIds() {
        when(vehicleRepository.findByBrandContainingIgnoreCaseAndGarageIdIn(anyString(), anyList()))
                .thenReturn(List.of(sampleVehicle));
        when(vehicleMapper.toResponseDTO(any(Vehicle.class))).thenReturn(sampleResponse);

        List<VehicleResponseDTO> result = vehicleService.getVehiclesByBrandAndGarages("Renault", List.of(1L, 2L));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(sampleResponse);

        verify(vehicleRepository).findByBrandContainingIgnoreCaseAndGarageIdIn("Renault", List.of(1L, 2L));
        verify(vehicleMapper).toResponseDTO(sampleVehicle);
    }
}

