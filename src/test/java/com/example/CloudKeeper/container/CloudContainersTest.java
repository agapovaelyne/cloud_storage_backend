package com.example.CloudKeeper.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = {CloudContainersTest.Initializer.class})
public class CloudContainersTest {

    private static final int APP_PORT = 8081;
    private static final int DB_PORT = 5432;
    private static final int FRONT_PORT = 8080;
    private static final String HOST = "http://localhost:";
    private final static Network cloudNetwork = Network.newNetwork();

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres")
            .withNetwork(cloudNetwork)
            .withExposedPorts(DB_PORT)
            .withNetworkAliases("cloud-db")
            .withDatabaseName("cloud_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static final GenericContainer<?> appContainer = new GenericContainer<>("cloudkeeper-app")
            .withNetwork(cloudNetwork)
            .withNetworkAliases("cloudkeeper-app")
            .withExposedPorts(APP_PORT)
            .withEnv(Map.of("SPRING_DATASOURCE_URL", "jdbc:postgresql://cloud-db:5432/cloud_db"))
            .dependsOn(dbContainer);

    @Container
    private static final GenericContainer<?> frontendContainer = new GenericContainer<>("cloudkeeper-frontend:latest")
            .withNetwork(cloudNetwork)
            .withNetworkAliases("cloudkeeper-frontend")
            .withExposedPorts(FRONT_PORT)
            .dependsOn(appContainer);

    @Test
    void db_is_running_test() {
        System.out.println(dbContainer.getJdbcUrl());
        Assertions.assertTrue(dbContainer.isRunning());
    }

    @Test
    void app_is_running() {
        var portBack = appContainer.getMappedPort(APP_PORT);
        ResponseEntity<String> entity = restTemplate.getForEntity(HOST + portBack, String.class);
        System.out.println(entity.getBody());
        Assertions.assertTrue(appContainer.isRunning());
    }

    @Test
    void frontend_is_running() {
        var portFront = frontendContainer.getMappedPort(8080);
        ResponseEntity<String> entity = restTemplate.getForEntity(HOST + portFront, String.class);
        System.out.println(entity.getBody());
        Assertions.assertTrue(frontendContainer.isRunning());
    }


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + dbContainer.getJdbcUrl(),
                    "spring.datasource.username=" + dbContainer.getUsername(),
                    "spring.datasource.password=" + dbContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}