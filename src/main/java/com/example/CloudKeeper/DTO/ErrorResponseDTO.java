package com.example.CloudKeeper.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private int id;
}
