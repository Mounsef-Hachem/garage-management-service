package com.renault.garage.mapper;


import com.renault.garage.dto.request.AccessoryRequestDTO;
import com.renault.garage.dto.response.AccessoryResponseDTO;
import com.renault.garage.model.Accessory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Accessory toEntity(AccessoryRequestDTO dto);

    AccessoryResponseDTO toResponseDTO(Accessory entity);

    List<Accessory> toEntityList(List<AccessoryResponseDTO> dtos);

    List<AccessoryResponseDTO> toResponseDTOList(List<Accessory> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccessory(AccessoryRequestDTO dto, @MappingTarget Accessory entity);
}
