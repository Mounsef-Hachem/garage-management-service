package com.renault.garage.mapper;

import com.renault.garage.dto.request.OpeningTimeDTO;
import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.model.DayOfWeek;
import com.renault.garage.model.Garage;
import com.renault.garage.model.OpeningTime;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    GarageResponseDTO toResponseDTO(Garage garage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Garage toEntity(GarageRequestDTO requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingHours", expression = "java(mapOpeningHoursToEntity(dto.openingHours()))")
    void updateGarageFromDto(GarageRequestDTO dto, @MappingTarget Garage garage);


    // Map<DayOfWeek, OpeningTime> → Map<DayOfWeek, OpeningTimeDTO>
    default Map<DayOfWeek, OpeningTimeDTO> mapOpeningHoursToDTO(
            Map<DayOfWeek, OpeningTime> openingHours
    ) {
        if (openingHours == null) return null;

        return openingHours.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            OpeningTime openingTime = entry.getValue();
                            return new OpeningTimeDTO(openingTime.getStartTime(), openingTime.getEndTime());
                        }
                ));
    }

    // Map<DayOfWeek, OpeningTimeDTO> → Map<DayOfWeek,OpeningTime>
    default Map<DayOfWeek, OpeningTime> mapOpeningHoursToEntity(
            Map<DayOfWeek, OpeningTimeDTO> openingHoursDTO
    ) {
        if (openingHoursDTO == null) return null;

       return openingHoursDTO.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            OpeningTimeDTO openingTimeDTO = entry.getValue();
                            return OpeningTime.builder()
                                    .startTime(openingTimeDTO.startTime())
                                    .endTime(openingTimeDTO.endTime())
                                    .build();
                        }
                ));
    }
}
