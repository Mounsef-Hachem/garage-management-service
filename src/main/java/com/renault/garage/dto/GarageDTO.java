package com.renault.garage.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageDTO {

    private Long id;

    @NotBlank(message = "Garage name is required.")
    private String name;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Phone number is required.")
    private String telephone;

    @Email(message = "Invalid email format.")
    private String email;

    /**
     * Opening hours per day of the week.
     * Key = DayOfWeek, Value = list of time slots.
     */
    private Map<String, List<OpeningTimeDTO>> openingHours = new HashMap<>();

    /**
     * List of vehicles managed by this garage.
     */
    private List<VehicleDTO> vehicles = new ArrayList<>();
}
