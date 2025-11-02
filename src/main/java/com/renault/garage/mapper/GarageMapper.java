package com.renault.garage.mapper;

import com.renault.garage.dto.OpeningTimeDTO;
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

//    @Mapping(target = "openingHours", expression = "java(mapOpeningHoursToDTO(garage.getOpeningHours()))")
    GarageResponseDTO toResponseDTO(Garage garage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingHours", expression = "java(mapOpeningHoursToEntity(requestDTO.openingHours()))")
    Garage toEntity(GarageRequestDTO requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingHours", expression = "java(mapOpeningHoursToEntity(dto.openingHours()))")
    void updateGarageFromDto(GarageRequestDTO dto, @MappingTarget Garage garage);


//    // Map<DayOfWeek, List<OpeningTime>> → Map<DayOfWeek, List<OpeningTimeDTO>>
    default Map<DayOfWeek, List<OpeningTimeDTO>> mapOpeningHoursToDTO(
            Map<DayOfWeek, OpeningTime> openingHours
    ) {
        if (openingHours == null) return null;
        return openingHours.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> List.of(new OpeningTimeDTO(
                                e.getValue().getStartTime(),
                                e.getValue().getEndTime()
                        ))
                ));
    }

    // Map<DayOfWeek, List<OpeningTimeDTO>> → Map<DayOfWeek, OpeningTime>
    default Map<DayOfWeek, OpeningTime> mapOpeningHoursToEntity(
            Map<DayOfWeek, List<OpeningTimeDTO>> openingHoursDTO
    ) {
        if (openingHoursDTO == null) return null;
        return openingHoursDTO.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().isEmpty() ? null : OpeningTime.builder()
                                .startTime(e.getValue().get(0).startTime())
                                .endTime(e.getValue().get(0).endTime())
                                .build()
                ));
    }
}
