package com.renault.garage.controller;

import com.renault.garage.dto.*;
import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.service.GarageService;
import com.renault.garage.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;
    private final VehicleService vehicleService;

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

    @Operation(summary = "Get supported vehicle types for a garage")
    @GetMapping("/{id}/supported-vehicle-types")
    public ResponseEntity<Set<String>> getSupportedVehicleTypes(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.getSupportedVehicleTypes(id));
    }

    @Operation(summary = "Search garages that have at least one vehicle containing an accessory with the given name")
    @GetMapping("/search/by-accessory")
    public ResponseEntity<List<GarageResponseDTO>> searchGaragesByAccessory(@RequestParam String name) {
        return ResponseEntity.ok(garageService.findGaragesByAccessory(name));
    }
}
