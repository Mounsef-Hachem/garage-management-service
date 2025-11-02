package com.renault.garage.repository;

import com.renault.garage.model.Vehicle;
import com.renault.garage.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByGarage(Garage garage);

    List<Vehicle> findByBrandIgnoreCase(String brand);

    List<Vehicle> findByBrandContainingIgnoreCase(String fuelType);

    List<Vehicle> findByBrandContainingIgnoreCaseAndGarageIdIn(String brand, List<Long> garageIds);

}
