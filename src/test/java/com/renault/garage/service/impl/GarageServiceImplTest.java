package com.renault.garage.service.impl;

import com.renault.garage.dto.request.*;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.model.*;
import com.renault.garage.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.stream.Collectors;

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

        Map<DayOfWeek, OpeningHourDTO> openingHours = new HashMap<>();

        openingHours.put(
                DayOfWeek.MONDAY,
                new OpeningHourDTO(
                        List.of(
                                new OpeningTimeDTO(
                                        LocalTime.of(8, 0),
                                        LocalTime.of(12, 0)
                                ),
                                new OpeningTimeDTO(
                                        LocalTime.of(14, 0),
                                        LocalTime.of(18, 0)
                                )
                        )
                )
        );

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

    private GarageRequestDTO request;
    private Garage garage;
    private GarageResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        request = createSampleGarageRequest();

        garage = Garage
                .builder()
                .id(1L)
                .name("Test Garage")
                .address("123 Test St")
                .telephone("+33123456789")
                .email("test@test.te")
                .supportedVehicleTypes(Set.of("car", "suv"))
                .openingHours(
                        Map.of(
                            DayOfWeek.MONDAY,
                            OpeningHour.builder()
                                    .openingTimes(List.of(
                                            OpeningTime.builder()
                                                    .startTime(LocalTime.of(8, 0))
                                                    .endTime(LocalTime.of(12, 0))
                                                    .build(),

                                            OpeningTime.builder()
                                                    .startTime(LocalTime.of(14, 0))
                                                    .endTime(LocalTime.of(18, 0))
                                                    .build()
                                    ))
                                    .build()
                ))
                .build();

        responseDTO = GarageResponseDTO
                .builder()
                .id(1L)
                .name("Test Garage")
                .address("123 Test St")
                .telephone("+33123456789")
                .email("test@test.te")
                .supportedVehicleTypes(Set.of("car", "suv"))
                .openingHours(
                        Map.of(
                                DayOfWeek.MONDAY,
                                OpeningHourDTO.builder()
                                        .openingTimes(List.of(
                                                new OpeningTimeDTO(LocalTime.of(8, 0),LocalTime.of(12, 0)),
                                                new OpeningTimeDTO(LocalTime.of(14, 0),LocalTime.of(18, 0))
                                        ))
                                        .build()
                        ))
                .build();
    }


    @Test
    void createGarage_shouldSaveAndReturnResponse() {

        when(garageMapper.toEntity(any(GarageRequestDTO.class))).thenReturn(garage);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.createGarage(request);

        assertThat(result).isEqualTo(responseDTO);

        verify(garageMapper).toEntity(request);
        verify(garageRepository).save(garage);
        verify(garageMapper).toResponseDTO(garage);
    }

    @Test
    void updateGarage_existing_shouldUpdateAndReturnResponse() {

        when(garageRepository.findById(anyLong())).thenReturn(Optional.of(garage));
        doAnswer(invocation -> {
            GarageRequestDTO dto = invocation.getArgument(0);
            Garage entity = invocation.getArgument(1);
            entity.setName(dto.name());
            entity.setTelephone(dto.telephone());
            entity.setEmail(dto.email());
            entity.setSupportedVehicleTypes(dto.supportedVehicleTypes());
            entity.setOpeningHours(
                    dto.openingHours().entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> {
                                        OpeningHourDTO openingHourDTO = entry.getValue();
                                        OpeningHour openingHour = new OpeningHour();
                                        List<OpeningTime> openingTimes = openingHourDTO.getOpeningTimes().stream()
                                                .map(openingTimeDTO -> {
                                                    OpeningTime openingTime = new OpeningTime();
                                                    openingTime.setStartTime(openingTimeDTO.startTime());
                                                    openingTime.setEndTime(openingTimeDTO.endTime());
                                                    openingTime.setOpeningHour(openingHour);
                                                    return openingTime;
                                                })
                                                .collect(Collectors.toList());
                                        openingHour.setOpeningTimes(openingTimes);
                                        return openingHour;
                                    }
                            )));
            return null;
        }).when(garageMapper).updateGarageFromDto(request, garage);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.updateGarage(1L, request);

        assertThat(result).isEqualTo(responseDTO);

        verify(garageRepository).findById(anyLong());
        verify(garageMapper).updateGarageFromDto(request, garage);
        verify(garageRepository).save(garage);
        verify(garageMapper).toResponseDTO(garage);
    }

    @Test
    void updateGarage_missing_shouldThrow() {

        when(garageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> garageService.updateGarage(24L, request));

        verify(garageRepository).findById(24L);
        verifyNoMoreInteractions(garageMapper);
    }

    @Test
    void getGarageById_existing_shouldReturnResponse() {

        when(garageRepository.findById(anyLong())).thenReturn(Optional.of(garage));
        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        GarageResponseDTO result = garageService.getGarageById(1L);

        assertThat(result).isEqualTo(responseDTO);

        verify(garageRepository).findById(1L);
        verify(garageMapper).toResponseDTO(garage);
    }

    @Test
    void getAllGarages_shouldReturnPageMappedToDtoList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

        when(garageRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(garage)));
        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        List<GarageResponseDTO> result = garageService.getAllGarages(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(responseDTO);

        verify(garageRepository).findAll(pageable);
        verify(garageMapper).toResponseDTO(garage);
    }

    @Test
    void deleteGarage_existing_shouldDelete() {
        when(garageRepository.existsById(anyLong())).thenReturn(true);

        garageService.deleteGarage(1L);

        verify(garageRepository).existsById(1L);
        verify(garageRepository).deleteById(1L);
    }

    @Test
    void deleteGarage_missing_shouldThrow() {

        when(garageRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> garageService.deleteGarage(1L));

        verify(garageRepository).existsById(1L);
        verify(garageRepository, never()).deleteById(1L);
    }

    @Test
    void getSupportedVehicleTypes_shouldReturnGarages() {

        when(garageRepository.findBySupportedVehicleTypesContainingIgnoreCase(anyString()))
                .thenReturn(List.of(garage));

        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        List<GarageResponseDTO> result = garageService.getSupportedVehicleTypes("SUV");

        assertThat(result).isEqualTo(List.of(responseDTO));

        verify(garageRepository).findBySupportedVehicleTypesContainingIgnoreCase("SUV");
        verify(garageMapper).toResponseDTO(garage);
    }

    @Test
    void findGaragesByAccessory_shouldReturnMappedDtos() {

        when(garageRepository.findByVehicles_Accessories_NameIgnoreCase(anyString()))
                .thenReturn(List.of(garage));

        when(garageMapper.toResponseDTO(any(Garage.class))).thenReturn(responseDTO);

        List<GarageResponseDTO> result = garageService.findGaragesByAccessory("GPS");

        assertThat(result).isEqualTo(List.of(responseDTO));

        verify(garageRepository).findByVehicles_Accessories_NameIgnoreCase("GPS");
        verify(garageMapper).toResponseDTO(garage);
    }
}
