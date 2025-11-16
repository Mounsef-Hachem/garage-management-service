package com.renault.garage.service;

import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface GarageService {

    GarageResponseDTO createGarage(GarageRequestDTO dto);

    GarageResponseDTO updateGarage(Long id, GarageRequestDTO dto);

    GarageResponseDTO getGarageById(Long id);

    Page<GarageResponseDTO> getAllGarages(Pageable pageable);

    void deleteGarage(Long id);

    List<GarageResponseDTO> getSupportedVehicleTypes(String vehicle_types);

    List<GarageResponseDTO> findGaragesByAccessory(String accessoryName);
}
