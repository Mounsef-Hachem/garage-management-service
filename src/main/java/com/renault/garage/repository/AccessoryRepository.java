package com.renault.garage.repository;

import com.renault.garage.model.Accessory;
import com.renault.garage.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findByVehicle(Vehicle vehicle);

    List<Accessory> findByVehicle_Id(Long vehicleId);

    List<Accessory> findByNameContainingIgnoreCase(String name);

    List<Accessory> findByTypeIgnoreCase(String type);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndVehicle_Garage_Id(String name, Long garageId);

    boolean existsByNameIgnoreCaseAndVehicleIsNotNull(String name);
}
