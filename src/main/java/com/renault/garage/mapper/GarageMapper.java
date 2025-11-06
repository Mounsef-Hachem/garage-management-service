package com.renault.garage.mapper;

import com.renault.garage.dto.request.OpeningHourDTO;
import com.renault.garage.dto.request.OpeningTimeDTO;
import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.model.DayOfWeek;
import com.renault.garage.model.Garage;
import com.renault.garage.model.OpeningHour;
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
    void updateGarageFromDto(GarageRequestDTO dto, @MappingTarget Garage garage);


    // Map<DayOfWeek, OpeningHourDTO> → Map<DayOfWeek, OpeningHourDTO>
    default Map<DayOfWeek, OpeningHourDTO> mapOpeningHoursToDTO(
            Map<DayOfWeek, OpeningHour> openingHours
    ) {
        if (openingHours == null) return null;

        return openingHours.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            OpeningHour openingHour = entry.getValue();
                            List<OpeningTimeDTO> openingTimeDTOs = openingHour.getOpeningTimes().stream()
                                    .map(openingTime -> new OpeningTimeDTO(
                                            openingTime.getStartTime(),
                                            openingTime.getEndTime()
                                    ))
                                    .collect(Collectors.toList());
                            OpeningHourDTO openingHourDTO = new OpeningHourDTO();
                            openingHourDTO.setOpeningTimes(openingTimeDTOs);
                            return openingHourDTO;
                        }
                ));
    }

    // Map<DayOfWeek, OpeningHourDTO> → Map<DayOfWeek,OpeningHour>
    default Map<DayOfWeek, OpeningHour> mapOpeningHoursToEntity(
            Map<DayOfWeek, OpeningHourDTO> openingHoursDTO
    ) {
        if (openingHoursDTO == null) return null;

        return openingHoursDTO.entrySet().stream()
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
                ));
    }
}
