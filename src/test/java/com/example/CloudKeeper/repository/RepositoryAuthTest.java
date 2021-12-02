package com.example.CloudKeeper.repository;

import com.example.CloudKeeper.model.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class RepositoryAuthTest {

    private final TestModels testModels = new TestModels();
    private final CloudRepository cloudRepository = new CloudRepository(testModels.getUserRepositoryMock(), testModels.getFileRepositoryMock());

    @Test
    void login_test() {
        cloudRepository.login(testModels.getAuthToken(), testModels.getUserPrincipal());
        assertTrue(cloudRepository.getTokenStorage().get(testModels.getAuthToken()).equals(testModels.getUserPrincipal()));
    }

    @Test
    void logout_test() {
        cloudRepository.login(testModels.getAuthToken(), testModels.getUserPrincipal());
        Optional<UserDetails> actual = cloudRepository.logout(testModels.getAuthToken());
        Optional<UserDetails> expected = Optional.of(testModels.getUserPrincipal());
        assertEquals(expected, actual);
    }

    @Test
    void logout_failed_test() {
        Optional<UserDetails> actual = cloudRepository.logout(testModels.getAuthToken());
        Optional<UserDetails> expected = Optional.empty();
        assertEquals(expected, actual);
    }

}
