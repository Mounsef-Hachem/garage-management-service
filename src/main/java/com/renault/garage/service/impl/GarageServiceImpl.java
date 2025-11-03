package com.renault.garage.service.impl;

import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.exception.ResourceNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.model.Garage;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    @Override
    public GarageResponseDTO createGarage(GarageRequestDTO dto) {
        Garage garage = garageMapper.toEntity(dto);
        garage = garageRepository.save(garage);
        return garageMapper.toResponseDTO(garage);
    }

    @Override
    public GarageResponseDTO updateGarage(Long id, GarageRequestDTO dto) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));

        garageMapper.updateGarageFromDto(dto, garage);
        garage = garageRepository.save(garage);
        return garageMapper.toResponseDTO(garage);
    }


    @Override
    @Transactional(readOnly = true)
    public GarageResponseDTO getGarageById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));

        return garageMapper.toResponseDTO(garage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageResponseDTO> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .stream()
                .map(garageMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void deleteGarage(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Garage not found with id: " + id);
        }
        garageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Garage> getSupportedVehicleTypes(String vehicleType) {
        return garageRepository.findBySupportedVehicleTypesContainingIgnoreCase(vehicleType)
                .stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageResponseDTO> findGaragesByAccessory(String accessoryName) {
        List<Garage> garages = garageRepository.findByVehicles_Accessories_NameIgnoreCase(accessoryName);
        return garages.stream().map(garageMapper::toResponseDTO).toList();
    }
}
