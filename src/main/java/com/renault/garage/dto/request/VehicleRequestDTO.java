package com.renault.garage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Represents the request for creating or updating a vehicle.")
public record VehicleRequestDTO(

        @Schema(
                description = "Vehicle brand",
                example = "Renault"
        )
        String brand,

        @Schema(
                description = "Year the vehicle was manufactured",
                example = "2022"
        )
        int manufacturingYear,

        @Schema(
                description = "Type of fuel used by the vehicle",
                example = "Gasoline"
        )
        String fuelType

) {}
