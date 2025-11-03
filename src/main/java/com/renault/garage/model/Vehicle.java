package com.renault.garage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;

    @NotBlank
    @Column(name = "fuel_type")
    private String fuelType;

    @ManyToOne()
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Accessory> accessories = new ArrayList<>();
}
