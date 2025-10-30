package com.renault.garage.repository;

import com.renault.garage.model.Accessory;
import com.renault.garage.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    List<Accessory> findByVehicle(Vehicle vehicle);

    List<Accessory> findByNameContainingIgnoreCase(String name);

    List<Accessory> findByTypeIgnoreCase(String type);
}
