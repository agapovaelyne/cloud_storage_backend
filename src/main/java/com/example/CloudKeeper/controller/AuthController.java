package com.example.CloudKeeper.controller;

import com.example.CloudKeeper.DTO.AuthorizationRequestDTO;
import com.example.CloudKeeper.DTO.AuthorizationTokenDTO;
import com.example.CloudKeeper.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationTokenDTO> login(@Valid @RequestBody AuthorizationRequestDTO authorization) {
        String authToken = authService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new AuthorizationTokenDTO(authToken), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);
        return HttpStatus.OK;
    }
}
