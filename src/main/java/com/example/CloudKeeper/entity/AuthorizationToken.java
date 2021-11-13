package com.example.CloudKeeper.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("auth-token")
    private String authToken;
}
