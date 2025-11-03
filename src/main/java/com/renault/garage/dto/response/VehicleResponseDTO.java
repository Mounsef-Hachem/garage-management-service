package com.renault.garage.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDTO {

    private Long id;
    private String brand;
    private Integer manufacturingYear;
    private String fuelType;
}
