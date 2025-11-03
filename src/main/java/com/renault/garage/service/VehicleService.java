package com.renault.garage.service;

import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;

import java.util.List;

public interface VehicleService {

    VehicleResponseDTO createVehicle(Long garageId, VehicleRequestDTO dto);

    VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto);

    List<VehicleResponseDTO> getVehicleByGarage(Long id);

    void deleteVehicle(Long id);

    List<VehicleResponseDTO> getVehiclesByBrandAndGarages(String model, List<Long> garageIds);

}
