package com.renault.garage.service;

import com.renault.garage.dto.request.AccessoryRequestDTO;
import com.renault.garage.dto.response.AccessoryResponseDTO;

import java.util.List;

public interface AccessoryService {

    AccessoryResponseDTO createAccessory(Long vehicleId, AccessoryRequestDTO dto);

    AccessoryResponseDTO updateAccessory(Long id, AccessoryRequestDTO dto);


    List<AccessoryResponseDTO> getAccessoriesByVehicle(Long vehicleId);

    void deleteAccessory(Long id);

    boolean isAccessoryAvailable(String name);

    boolean isAccessoryAvailableInGarage(Long garageId, String name);
}
