package com.renault.garage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Represents an opening time slot for a specific day.")
public record OpeningTimeDTO(

        @Schema(
                description = "Start time of the opening period",
                example = "08:00:00"
        )
        LocalTime startTime,

        @Schema(
                description = "End time of the opening period",
                example = "12:00:00"
        )
        LocalTime endTime
) {}
