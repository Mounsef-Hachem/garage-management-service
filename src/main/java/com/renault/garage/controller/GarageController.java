package com.renault.garage.controller;


import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.model.Garage;
import com.renault.garage.service.GarageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @Operation(summary = "Create a new garage")
    @PostMapping
    public ResponseEntity<GarageResponseDTO> createGarage(@RequestBody GarageRequestDTO dto) {
        return ResponseEntity.ok(garageService.createGarage(dto));
    }

    @Operation(summary = "Update an existing garage by ID")
    @PutMapping("/{id}")
    public ResponseEntity<GarageResponseDTO> updateGarage(@PathVariable Long id, @RequestBody GarageRequestDTO dto) {
        return ResponseEntity.ok(garageService.updateGarage(id, dto));
    }

    @Operation(summary = "Get garage details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GarageResponseDTO> getGarageById(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.getGarageById(id));
    }

    @Operation(summary = "get all paginated with sorting options (by name, city, etc.).")
    @GetMapping
    public ResponseEntity<List<GarageResponseDTO>> getAllGarages(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(garageService.getAllGarages(pageable));
    }

    @Operation(summary = "Delete a garage by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get garage by supported vehicle")
    @GetMapping("/supported-vehicle/{vehicleType}")
    public ResponseEntity<List<Garage>> getSupportedVehicleTypes(@PathVariable String vehicleType) {
        return ResponseEntity.ok(garageService.getSupportedVehicleTypes(vehicleType));
    }

    @Operation(summary = "Search garages that have at least one vehicle containing an accessory with the given name")
    @GetMapping("/search/by-accessory")
    public ResponseEntity<List<GarageResponseDTO>> searchGaragesByAccessory(@RequestParam String name) {
        return ResponseEntity.ok(garageService.findGaragesByAccessory(name));
    }
}
