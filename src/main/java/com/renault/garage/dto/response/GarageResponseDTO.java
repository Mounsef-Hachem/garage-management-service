package com.renault.garage.dto.response;

import com.renault.garage.dto.request.OpeningHourDTO;
import com.renault.garage.dto.request.OpeningTimeDTO;
import com.renault.garage.model.DayOfWeek;
import com.renault.garage.model.OpeningHour;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageResponseDTO {

    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private Map<DayOfWeek, OpeningHourDTO> openingHours;
    private Set<String> supportedVehicleTypes;
}
