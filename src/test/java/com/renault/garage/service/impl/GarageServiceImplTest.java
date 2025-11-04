package com.renault.garage.service.impl;

import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.request.OpeningTimeDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.model.DayOfWeek;
import com.renault.garage.model.Garage;
import com.renault.garage.repository.GarageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private GarageRequestDTO createSampleGarageRequest() {
        Map<DayOfWeek, List<OpeningTimeDTO>> openingHours = new HashMap<>();
        openingHours.put(DayOfWeek.MONDAY, List.of(new OpeningTimeDTO(LocalTime.of(8, 0), LocalTime.of(12, 0))));
        Set<String> supported = new HashSet<>();
        supported.add("car");
        supported.add("suv");
        return new GarageRequestDTO(
                "Test Garage",
                "123 Test St",
                "+33123456789",
                "test@example.com",
                openingHours,
                supported
        );
    }

    @Test
    void createGarage_shouldSaveAndReturnResponse() {
        GarageRequestDTO request = createSampleGarageRequest();
        Garage entity = new Garage();
        Garage saved = new Garage();
        GarageResponseDTO responseDTO = new GarageResponseDTO();

        when(garageMapper.toEntity(request)).thenReturn(entity);
        when(garageRepository.save(entity)).thenReturn(saved);
        when(garageMapper.toResponseDTO(saved)).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.createGarage(request);

        assertThat(result).isSameAs(responseDTO);
        verify(garageMapper).toEntity(request);
        verify(garageRepository).save(entity);
        verify(garageMapper).toResponseDTO(saved);
    }

    @Test
    void updateGarage_existing_shouldUpdateAndReturnResponse() {
        Long id = 1L;
        GarageRequestDTO request = createSampleGarageRequest();
        Garage existing = new Garage();
        Garage saved = new Garage();
        GarageResponseDTO responseDTO = new GarageResponseDTO();

        when(garageRepository.findById(id)).thenReturn(Optional.of(existing));
        // updateGarageFromDto is a void; we just verify it is called
        doNothing().when(garageMapper).updateGarageFromDto(request, existing);
        when(garageRepository.save(existing)).thenReturn(saved);
        when(garageMapper.toResponseDTO(saved)).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.updateGarage(id, request);

        assertThat(result).isEqualTo(responseDTO);

        verify(garageRepository).findById(id);
        verify(garageMapper).updateGarageFromDto(request, existing);
        verify(garageRepository).save(existing);
        verify(garageMapper).toResponseDTO(saved);
    }

    @Test
    void updateGarage_missing_shouldThrow() {
        Long id = 42L;
        GarageRequestDTO request = createSampleGarageRequest();
        when(garageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garageService.updateGarage(id, request));

        verify(garageRepository).findById(id);
        verifyNoMoreInteractions(garageMapper);
    }

    @Test
    void getGarageById_existing_shouldReturnResponse() {
        Long id = 2L;
        Garage entity = new Garage();
        GarageResponseDTO responseDTO = new GarageResponseDTO();

        when(garageRepository.findById(id)).thenReturn(Optional.of(entity));
        when(garageMapper.toResponseDTO(entity)).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.getGarageById(id);

        assertThat(result).isEqualTo(responseDTO);

        verify(garageRepository).findById(id);
        verify(garageMapper).toResponseDTO(entity);
    }

    @Test
    void getAllGarages_shouldReturnPageMappedToDtoList() {
        Garage g1 = new Garage();
        Garage g2 = new Garage();
        Page<Garage> page = new PageImpl<>(List.of(g1, g2));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        when(garageRepository.findAll(pageable)).thenReturn(page);
        when(garageMapper.toResponseDTO(g1)).thenReturn(new GarageResponseDTO());
        when(garageMapper.toResponseDTO(g2)).thenReturn(new GarageResponseDTO());

        List<GarageResponseDTO> result = garageService.getAllGarages(pageable);

        assertThat(result).hasSize(2);

        verify(garageRepository).findAll(pageable);
        verify(garageMapper, times(2)).toResponseDTO(any(Garage.class));
    }

    @Test
    void deleteGarage_existing_shouldDelete() {
        Long id = 5L;
        when(garageRepository.existsById(id)).thenReturn(true);

        garageService.deleteGarage(id);

        verify(garageRepository).existsById(id);
        verify(garageRepository).deleteById(id);
    }

    @Test
    void deleteGarage_missing_shouldThrow() {
        Long id = 6L;
        when(garageRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> garageService.deleteGarage(id));

        verify(garageRepository).existsById(id);
        verify(garageRepository, never()).deleteById(any());
    }

    @Test
    void getSupportedVehicleTypes_shouldReturnGarages() {
        String vehicleType = "SUV";
        Garage g1 = new Garage();
        Garage g2 = new Garage();
        GarageResponseDTO dto1 = new GarageResponseDTO();
        GarageResponseDTO dto2 = new GarageResponseDTO();

        when(garageRepository.findBySupportedVehicleTypesContainingIgnoreCase(vehicleType))
                .thenReturn(List.of(g1, g2));

        when(garageMapper.toResponseDTO(any(Garage.class))).thenAnswer(invocation -> {
            Garage arg = invocation.getArgument(0);
            if (arg == g1) return dto1;
            if (arg == g2) return dto2;
            return new GarageResponseDTO();
        });

        List<GarageResponseDTO> result = garageService.getSupportedVehicleTypes(vehicleType);

        assertThat(result).containsExactly(dto1, dto2);

        verify(garageRepository).findBySupportedVehicleTypesContainingIgnoreCase(vehicleType);
        verify(garageMapper, times(2)).toResponseDTO(any(Garage.class));
    }

    @Test
    void findGaragesByAccessory_shouldReturnMappedDtos() {
        String accessoryName = "GPS";
        Garage g1 = new Garage();
        Garage g2 = new Garage();
        GarageResponseDTO dto1 = new GarageResponseDTO();
        GarageResponseDTO dto2 = new GarageResponseDTO();

        when(garageRepository.findByVehicles_Accessories_NameIgnoreCase(accessoryName))
                .thenReturn(List.of(g1, g2));

        // Return different DTOs depending on the garage instance
        when(garageMapper.toResponseDTO(any(Garage.class))).thenAnswer(invocation -> {
            Garage arg = invocation.getArgument(0);
            if (arg == g1) return dto1;
            if (arg == g2) return dto2;
            return new GarageResponseDTO();
        });

        List<GarageResponseDTO> result = garageService.findGaragesByAccessory(accessoryName);

        assertThat(result).containsExactlyInAnyOrder(dto1, dto2);

        verify(garageRepository).findByVehicles_Accessories_NameIgnoreCase(accessoryName);
        verify(garageMapper, times(2)).toResponseDTO(any(Garage.class));
    }
}
