package com.renault.garage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "garages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @Email
    @Column(nullable = false)
    private String email;

    /**
     * Key = day of the week
     * Value = list of opening slots
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "garage_opening_hours", joinColumns = @JoinColumn(name = "garage_id"))
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, List<OpeningTime>> OpeningTimes = new HashMap<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();
}
