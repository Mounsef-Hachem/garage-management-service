package com.renault.garage.controller;

import com.renault.garage.dto.*;
import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.service.VehicleService;
import com.renault.garage.service.AccessoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Add a vehicle to a specific garage")
    @PostMapping("/{garageId}")
    public ResponseEntity<VehicleResponseDTO> addVehicleToGarage(
            @PathVariable Long garageId,
            @RequestBody VehicleRequestDTO vehicleRequestDTO) {
        return ResponseEntity.ok(vehicleService.createVehicle(garageId, vehicleRequestDTO));
    }

    @Operation(summary = "Update an existing vehicle by ID")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Long id, @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, dto));
    }

    @Operation(summary = "Delete a vehicle by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all vehicles of a given garage")
    @GetMapping("/garage/{id}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByGarage(
            @PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleByGarage(id));
    }


    @Operation(summary = "Get all vehicles of a given brand across optional garages")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByBrand(
            @PathVariable String brand,
            @RequestParam(required = false) List<Long> garageIds) {
        return ResponseEntity.ok(vehicleService.getVehiclesByBrandAndGarages(brand, garageIds));
    }


}
