package com.renault.garage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String brand;

    @NotBlank
    private int yearOfManufacture;

    @NotBlank
    @Column(name = "fuel_type")
    private String fuelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicle ", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accessory> accessories = new ArrayList<>();
}
