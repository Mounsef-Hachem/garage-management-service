package com.renault.garage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents an accessory associated with a vehicle.")
public record AccessoryRequestDTO(

        @Schema(
                description = "Name of the accessory",
                example = "Built-in GPS"
        )
        String name,

        @Schema(
                description = "Detailed description of the accessory",
                example = "GPS navigation system with automatic map updates."
        )
        String description,

        @Schema(
                description = "Price of the accessory in euros",
                example = "499.99"
        )
        double price,

        @Schema(
                description = "Type of accessory (e.g., safety, comfort, multimedia, etc.)",
                example = "Multimedia"
        )
        String type
) {}
