package com.renault.garage.dto.request;

import com.renault.garage.model.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Schema(description = "Represents the request for creating a new garage.")
public record GarageRequestDTO(

        @Schema(
                description = "Name of the garage",
                example = "Renault Casablanca Service Center"
        )
        @Size(max = 100, message = "Garage name must be at most 100 characters.")
        String name,

        @Schema(
                description = "Full address of the garage",
                example = "123 Mohammed V Avenue, Casablanca, Morocco"
        )
        @Size(max = 255, message = "Address must be at most 255 characters.")
        String address,

        @Schema(
                description = "Garage telephone number",
                example = "+212522334455"
        )
        @Size(max = 20, message = "Telephone must be at most 20 characters.")
        String telephone,

        @Schema(
                description = "Garage email address",
                example = "contact@renault-casa.ma"
        )
        @Email(message = "Invalid email format.")
        String email,


        @Schema(
                description = "Garage opening hours organized by day of the week",
                example = """
                    {
                      "MONDAY": {
                        "openingTimes": [
                          { "startTime": "08:00:00", "endTime": "12:00:00" },
                          { "startTime": "14:00:00", "endTime": "18:00:00" }
                        ]
                      },
                      "TUESDAY": {
                        "openingTimes": [
                          { "startTime": "08:00:00", "endTime": "12:00:00" },
                          { "startTime": "14:00:00", "endTime": "18:00:00" }
                        ]
                      },
                      "WEDNESDAY": {
                        "openingTimes": [
                          { "startTime": "08:00:00", "endTime": "12:00:00" },
                          { "startTime": "14:00:00", "endTime": "18:00:00" }
                        ]
                      }
                    }
                """
        )
        Map<DayOfWeek, OpeningHourDTO> openingHours,

        @Schema(
                description = "Types of vehicles supported by this garage",
                example = "[\"car\", \"suv\"]"
        )
        Set<String> supportedVehicleTypes
) {}
