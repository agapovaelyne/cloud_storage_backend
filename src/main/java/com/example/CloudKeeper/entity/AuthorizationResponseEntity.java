package com.example.CloudKeeper.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponseEntity {
    @Getter
    @Setter
    private String authToken; //TODO: передать имя правильно
}
