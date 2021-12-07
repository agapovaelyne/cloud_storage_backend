package com.example.CloudKeeper.service;

import com.example.CloudKeeper.exception.CloudException;
import com.example.CloudKeeper.model.TestModels;
import com.example.CloudKeeper.repository.CloudRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class ServiceAuthTest {

    private final TestModels testModels = new TestModels();
    private final CloudRepository cloudRepositoryMock = mock(CloudRepository.class);
    private final AuthService authService = new AuthService(cloudRepositoryMock, testModels.getAuthenticationManagerMock(), testModels.getJwtUtilsMock());

    @Test
    void login_test() {
        String actual = authService.login(testModels.getAuthRequest());
        String expected = testModels.getAuthToken();
        assertEquals(expected, actual);
    }

    @Test
    void logout_test() {
        Mockito.when(cloudRepositoryMock.logout(testModels.getAuthToken())).thenReturn(of(testModels.getUserPrincipal()));
        assertDoesNotThrow(() -> authService.logout(testModels.getAuthToken()));
    }

    @Test
    void logout_failed_test2() {
        String expected = CloudException.class.getName();
        CloudException exception = assertThrows(CloudException.class,
                () -> authService.logout(testModels.getAuthToken())
        );
        assertEquals(expected, exception.getClass().getName());
    }

}
