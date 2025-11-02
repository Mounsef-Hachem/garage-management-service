package com.renault.garage.service.impl;

import com.renault.garage.dto.*;
import com.renault.garage.dto.request.AccessoryRequestDTO;
import com.renault.garage.dto.response.AccessoryResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.model.Accessory;
import com.renault.garage.model.Vehicle;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryResponseDTO createAccessory(Long vehicleId, AccessoryRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        Accessory accessory = accessoryMapper.toEntity(dto);
        accessory.setVehicle(vehicle);

        return accessoryMapper.toResponseDTO(accessoryRepository.save(accessory));
    }

    @Override
    public AccessoryResponseDTO updateAccessory(Long id, AccessoryRequestDTO dto) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accessory not found with id: " + id));

        accessoryMapper.updateAccessory(dto, accessory);
        return accessoryMapper.toResponseDTO(accessoryRepository.save(accessory));
    }

    @Override
    public List<AccessoryResponseDTO> getAccessoriesByVehicle(Long vehicleId) {
        return accessoryRepository.findByVehicle_Id(vehicleId)
                .stream()
                .map(accessoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void deleteAccessory(Long id) {
        if (!accessoryRepository.existsById(id)) {
            throw new RuntimeException("Accessory not found with id: " + id);
        }
        accessoryRepository.deleteById(id);
    }

    @Override
    public boolean isAccessoryAvailable(String name) {
        return accessoryRepository.existsByNameIgnoreCaseAndVehicleIsNotNull(name);
    }

    @Override
    public boolean isAccessoryAvailableInGarage(Long garageId, String name) {
        return accessoryRepository.existsByNameIgnoreCaseAndVehicle_Garage_Id(name, garageId);
    }
}
