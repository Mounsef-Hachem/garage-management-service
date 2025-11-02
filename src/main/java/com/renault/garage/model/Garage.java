package com.renault.garage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private String name;
    private String address;
    private String telephone;
    private String email;

    @ElementCollection
    @CollectionTable(name = "garage_opening_times", joinColumns = @JoinColumn(name = "garage_id"))
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, OpeningTime> openingHours = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "garage_supported_vehicle_types", joinColumns = @JoinColumn(name = "garage_id"))
    @Column(name = "vehicle_type")
    private Set<String> supportedVehicleTypes = new HashSet<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}
