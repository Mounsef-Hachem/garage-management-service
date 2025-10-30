package com.renault.garage.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private Long id;

    @NotBlank(message = "Brand is required.")
    private String brand;

    @Min(value = 1950, message = "Manufacturing year must be after 1950.")
    private int manufacturingYear;

    @NotBlank(message = "Fuel type is required.")
    private String fuelType;

    /**
     * reference to the garage ID this vehicle belongs to.
     */
    private Long garageId;

    /**
     * List of accessories for this vehicle.
     */
    private List<AccessoryDTO> accessories = new ArrayList<>();
}
