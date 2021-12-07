package com.example.CloudKeeper.controller;

import com.example.CloudKeeper.DTO.AuthorizationTokenDTO;
import com.example.CloudKeeper.model.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ControllerAuthTest {

    private final TestModels testModels = new TestModels();
    private final AuthController authController = new AuthController(testModels.getAuthServiceMock());

    @Test
    void login_test() {
        ResponseEntity<AuthorizationTokenDTO> actual = authController.login(testModels.getAuthRequest());
        ResponseEntity<AuthorizationTokenDTO> expected = new ResponseEntity<>(testModels.getAuthTokenDTO(), HttpStatus.OK);
        assertEquals(expected, actual);
    }

    @Test
    void login_failed_test() {
        ResponseEntity<AuthorizationTokenDTO> actual = authController.login(testModels.getBadAuthRequest());
        ResponseEntity<AuthorizationTokenDTO> expected = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        assertEquals(expected, actual);
    }

    @Test
    void logout_test() {
        HttpStatus actual = authController.logout(testModels.getAuthToken());
        HttpStatus expected = HttpStatus.OK;
        assertEquals(expected, actual);
    }
}
