package com.renault.garage.controller;


import com.renault.garage.dto.request.GarageRequestDTO;
import com.renault.garage.dto.response.GarageResponseDTO;
import com.renault.garage.service.GarageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
    @PutMapping("/{garageId}")
    public ResponseEntity<GarageResponseDTO> updateGarage(@PathVariable Long garageId, @RequestBody GarageRequestDTO dto) {
        return ResponseEntity.ok(garageService.updateGarage(garageId, dto));
    }

    @Operation(summary = "Get garage details by ID")
    @GetMapping("/{garageId}")
    public ResponseEntity<GarageResponseDTO> getGarageById(@PathVariable Long garageId) {
        return ResponseEntity.ok(garageService.getGarageById(garageId));
    }

    @Operation(summary = "get all paginated with sorting options (by name, city, etc.).")
    @GetMapping
    public ResponseEntity<Page<GarageResponseDTO>> getAllGarages(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(garageService.getAllGarages(pageable));
    }

    @Operation(summary = "Delete a garage by ID")
    @DeleteMapping("/{garageId}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long garageId) {
        garageService.deleteGarage(garageId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get garage by supported vehicle")
    @GetMapping("/supported-vehicle/{vehicleType}")
    public ResponseEntity<List<GarageResponseDTO>> getSupportedVehicleTypes(@PathVariable String vehicleType) {
        return ResponseEntity.ok(garageService.getSupportedVehicleTypes(vehicleType));
    }

    @Operation(summary = "Search garages that have at least one vehicle containing an accessory with the given name")
    @GetMapping("/search-by-accessory/{accessoryName}")
    public ResponseEntity<List<GarageResponseDTO>> searchGaragesByAccessory(@PathVariable String accessoryName) {
        return ResponseEntity.ok(garageService.findGaragesByAccessory(accessoryName));
    }
}
