package com.renault.garage.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accessories")
@Builder
@Setter
@Getter
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
