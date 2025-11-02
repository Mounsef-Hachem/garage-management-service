package com.renault.garage.service;

import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface GarageService {

    GarageResponseDTO createGarage(GarageRequestDTO dto);

    GarageResponseDTO updateGarage(Long id, GarageRequestDTO dto);

    GarageResponseDTO getGarageById(Long id);

    List<GarageResponseDTO> getAllGarages(Pageable pageable);

    void deleteGarage(Long id);

    Set<String> getSupportedVehicleTypes(Long garageId);

    List<GarageResponseDTO> findGaragesByAccessory(String accessoryName);
}
