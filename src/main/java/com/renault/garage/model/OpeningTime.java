package com.renault.garage.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningTime {

    private LocalTime startTime;
    private LocalTime endTime;
}