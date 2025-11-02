package com.renault.garage.mapper;

import com.renault.garage.dto.request.VehicleRequestDTO;
import com.renault.garage.dto.response.VehicleResponseDTO;
import com.renault.garage.model.Vehicle;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {AccessoryMapper.class})
public interface VehicleMapper {

    Vehicle toEntity(VehicleRequestDTO dto);

    VehicleResponseDTO toResponseDTO(Vehicle entity);

    @Mapping(target = "accessories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVehicleFromDTO(VehicleRequestDTO dto, @MappingTarget Vehicle entity);

    // Added: map lists of entities/dtos
    List<VehicleResponseDTO> toVehicleResponseDTOList(List<Vehicle> entities);

    // Keep original name used by service impl
    List<VehicleResponseDTO> toResponseDTOList(List<Vehicle> entities);

    List<Vehicle> toEntityList(List<VehicleRequestDTO> dtos);
}
