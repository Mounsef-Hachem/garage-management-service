package com.renault.garage.dto.response;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String type;
}
