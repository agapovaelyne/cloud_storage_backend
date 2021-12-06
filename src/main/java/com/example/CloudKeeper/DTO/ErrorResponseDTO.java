package com.example.CloudKeeper.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int id;
}
