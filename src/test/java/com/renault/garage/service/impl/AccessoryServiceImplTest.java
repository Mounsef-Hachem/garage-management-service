package com.renault.garage.service.impl;

import com.renault.garage.dto.request.AccessoryRequestDTO;
import com.renault.garage.dto.response.AccessoryResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.model.Accessory;
import com.renault.garage.model.Vehicle;
import com.renault.garage.repository.AccessoryRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessoryServiceImplTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    private Vehicle vehicle;
    private AccessoryRequestDTO requestDTO;
    private Accessory accessoryEntity;
    private AccessoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setId(1L);

        requestDTO = new AccessoryRequestDTO("GPS", "Navigator", 199.99, "Multimedia");

        accessoryEntity = new Accessory();
        accessoryEntity.setName("GPS");
        accessoryEntity.setDescription("Navigator");
        accessoryEntity.setPrice(199.99);
        accessoryEntity.setType("Multimedia");

        responseDTO = AccessoryResponseDTO.builder()
                .id(5L)
                .name("GPS")
                .description("Navigator")
                .price(199.99)
                .type("Multimedia")
                .build();
    }

    @Test
    void createAccessory_success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(requestDTO)).thenReturn(accessoryEntity);
        when(accessoryRepository.save(any(Accessory.class))).thenAnswer(invocation -> {
            Accessory a = invocation.getArgument(0);
            a.setId(5L);
            return a;
        });
        when(accessoryMapper.toResponseDTO(any(Accessory.class))).thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.createAccessory(1L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(vehicleRepository).findById(1L);
        verify(accessoryRepository).save(any(Accessory.class));
        verify(accessoryMapper).toResponseDTO(any(Accessory.class));
    }

    @Test
    void createAccessory_vehicleNotFound_shouldThrow() {
        when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accessoryService.createAccessory(2L, requestDTO));

        verify(vehicleRepository).findById(2L);
        verifyNoInteractions(accessoryRepository);
    }

    @Test
    void updateAccessory_success() {
        Accessory existing = new Accessory();
        existing.setId(7L);
        when(accessoryRepository.findById(7L)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            AccessoryRequestDTO dto = invocation.getArgument(0);
            Accessory ent = invocation.getArgument(1);
            ent.setName(dto.name());
            ent.setDescription(dto.description());
            ent.setPrice(dto.price());
            ent.setType(dto.type());
            return null;
        }).when(accessoryMapper).updateAccessory(any(AccessoryRequestDTO.class), any(Accessory.class));
        when(accessoryRepository.save(existing)).thenReturn(existing);
        when(accessoryMapper.toResponseDTO(existing)).thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.updateAccessory(7L, requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(accessoryRepository).findById(7L);
        verify(accessoryMapper).updateAccessory(any(AccessoryRequestDTO.class), any(Accessory.class));
        verify(accessoryRepository).save(existing);
        verify(accessoryMapper).toResponseDTO(existing);
    }

    @Test
    void updateAccessory_notFound_shouldThrow() {
        when(accessoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accessoryService.updateAccessory(99L, requestDTO));

        verify(accessoryRepository).findById(99L);
    }

    @Test
    void getAccessoriesByVehicle_returnsList() {
        when(accessoryRepository.findByVehicle_Id(1L)).thenReturn(List.of(accessoryEntity));
        when(accessoryMapper.toResponseDTO(any(Accessory.class))).thenReturn(responseDTO);

        List<AccessoryResponseDTO> result = accessoryService.getAccessoriesByVehicle(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(responseDTO);
        verify(accessoryRepository).findByVehicle_Id(1L);
        verify(accessoryMapper).toResponseDTO(any(Accessory.class));
    }

    @Test
    void deleteAccessory_existing_shouldDelete() {
        when(accessoryRepository.existsById(5L)).thenReturn(true);

        accessoryService.deleteAccessory(5L);

        verify(accessoryRepository).existsById(5L);
        verify(accessoryRepository).deleteById(5L);
    }

    @Test
    void deleteAccessory_missing_shouldThrow() {
        when(accessoryRepository.existsById(8L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> accessoryService.deleteAccessory(8L));

        verify(accessoryRepository).existsById(8L);
        verify(accessoryRepository, never()).deleteById(anyLong());
    }
}

