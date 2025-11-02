package com.renault.garage.dto.response;

import com.renault.garage.dto.OpeningTimeDTO;
import com.renault.garage.model.DayOfWeek;
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
    private Map<DayOfWeek, List<OpeningTimeDTO>> openingHours;
    private Set<String> supportedVehicleTypes;
}
