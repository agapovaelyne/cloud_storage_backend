package com.example.CloudKeeper.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationRequestDTO {

    @NotBlank(message = "login couldn't be blank")
    @Size(min = 2, max = 30)
    private String login;

    @NotBlank(message = "login couldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-ZА-Я]).{8,}$")
    private String password;
}
