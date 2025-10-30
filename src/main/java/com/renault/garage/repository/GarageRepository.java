package com.renault.garage.repository;

import com.renault.garage.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long>, JpaSpecificationExecutor<Garage> {

    Optional<Garage> findByName(String name);

    List<Garage> findByAddressContainingIgnoreCase(String keyword);

    boolean existsByEmail(String email);
}
