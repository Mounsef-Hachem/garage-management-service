package com.renault.garage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHour {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "openingHour", cascade = CascadeType.ALL)
    private List<OpeningTime> openingTimes = new ArrayList<>();

}
