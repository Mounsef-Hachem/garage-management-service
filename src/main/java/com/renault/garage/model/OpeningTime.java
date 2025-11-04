package com.renault.garage.model;


import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OpeningTime {

    private LocalTime startTime;
    private LocalTime endTime;
}