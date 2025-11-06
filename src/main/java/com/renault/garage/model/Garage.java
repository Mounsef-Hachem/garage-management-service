package com.renault.garage.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "garages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String telephone;
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @JoinColumn(name = "garage_id")
    private Map<DayOfWeek, OpeningHour> openingHours = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "garage_supported_vehicle_types", joinColumns = @JoinColumn(name = "garage_id"))
    @Column(name = "vehicle_type")
    private Set<String> supportedVehicleTypes = new HashSet<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}
