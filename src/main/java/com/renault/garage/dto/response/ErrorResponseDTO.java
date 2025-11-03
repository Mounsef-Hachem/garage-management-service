package com.renault.garage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private String status;
    private String error;
    private String message;
    private String path;

}
