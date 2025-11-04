package com.renault.garage.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleResponseDTO {

    private Long id;
    private String brand;
    private Integer manufacturingYear;
    private String fuelType;
}
