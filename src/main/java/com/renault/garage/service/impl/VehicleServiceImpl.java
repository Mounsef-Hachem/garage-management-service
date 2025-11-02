package com.renault.garage.service.impl;

import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.model.Garage;
import com.renault.garage.model.Vehicle;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public VehicleResponseDTO createVehicle(Long garageId, VehicleRequestDTO dto) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setGarage(garage);

        return vehicleMapper.toResponseDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicleMapper.updateVehicleFromDTO(dto, vehicle);
        return vehicleMapper.toResponseDTO(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional()
    public List<VehicleResponseDTO> getVehicleByGarage(Long garageId) {

        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        List<Vehicle> vehicle = vehicleRepository.findByGarage(garage)
                .stream().toList();

        return vehicleMapper.toVehicleResponseDTOList(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByBrandAndGarages(String brand, List<Long> garageIds) {
        List<Vehicle> vehicles;
        if (garageIds == null || garageIds.isEmpty()) {
            vehicles = vehicleRepository.findByBrandContainingIgnoreCase(brand);
        } else {
            vehicles = vehicleRepository.findByBrandContainingIgnoreCaseAndGarageIdIn(brand, garageIds);
        }
        return vehicles.stream()
                .map(vehicleMapper::toResponseDTO)
                .collect(Collectors.toList());

    }
}
