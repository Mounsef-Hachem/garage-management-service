package com.renault.garage.controller;


import com.renault.garage.dto.request.AccessoryRequestDTO;
import com.renault.garage.dto.response.AccessoryResponseDTO;
import com.renault.garage.service.AccessoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    @Operation(summary = "Add an accessory to a specific vehicle")
    @PostMapping("/{vehicleId}")
    public ResponseEntity<AccessoryResponseDTO> addAccessoryToVehicle(
            @PathVariable Long vehicleId,
            @RequestBody AccessoryRequestDTO dto) {
        return ResponseEntity.ok(accessoryService.createAccessory(vehicleId, dto));
    }

    @Operation(summary = "Update an existing accessory by ID")
    @PutMapping("/{id}")
    public ResponseEntity<AccessoryResponseDTO> updateAccessory(@PathVariable Long id, @RequestBody AccessoryRequestDTO dto) {
        return ResponseEntity.ok(accessoryService.updateAccessory(id, dto));
    }

    @Operation(summary = "Delete an accessory by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all accessories for a specific vehicle")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<List<AccessoryResponseDTO>> getAccessoriesByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.getAccessoriesByVehicle(vehicleId));
    }

}
