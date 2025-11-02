package com.renault.garage.mapper;

import com.renault.garage.model.OpeningTime;
import com.renault.garage.dto.request.OpeningTimeDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OpeningTime toEntity(OpeningTimeDTO dto);

    OpeningTimeDTO toDto(OpeningTime entity);

    List<OpeningTimeDTO> toDtoList(List<OpeningTime> entities);

    List<OpeningTime> toEntityList(List<OpeningTimeDTO> dtos);
}
