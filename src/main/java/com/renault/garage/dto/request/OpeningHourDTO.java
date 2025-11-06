package com.renault.garage.dto.request;



import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHourDTO {

    List<OpeningTimeDTO> openingTimes;
}
