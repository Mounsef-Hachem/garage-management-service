package com.renault.garage.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryDTO {

    private Long id;

    @NotBlank(message = "Accessory name is required.")
    private String name;

    private String description;

    @Positive(message = "Price must be greater than zero.")
    private double price;

    @NotBlank(message = "Accessory type is required.")
    private String type;

    /**
     * reference to the vehicle ID this accessory belongs to.
     */
    private Long vehicleId;
}
