package com.renault.garage.controller;


import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.service.VehicleService;
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
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleRequestDTO dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicleId, dto));
    }

    @Operation(summary = "Delete a vehicle by ID")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all vehicles of a given garage")
    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByGarage(
            @PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.getVehicleByGarage(garageId));
    }


    @Operation(summary = "Get all vehicles of a given brand across optional garages")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByBrand(
            @PathVariable String brand,
            @RequestParam(required = false) List<Long> garageIds) {
        return ResponseEntity.ok(vehicleService.getVehiclesByBrandAndGarages(brand, garageIds));
    }


}
