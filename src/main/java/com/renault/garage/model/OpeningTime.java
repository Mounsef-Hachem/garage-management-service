package com.renault.garage.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OpeningTime {
    @Id
    @GeneratedValue
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne()
    private OpeningHour openingHour;
}