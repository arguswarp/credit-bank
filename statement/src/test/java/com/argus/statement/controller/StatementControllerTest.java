package com.argus.statement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * StatementControllerTest
 *
 * @author Maxim Chistyakov
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
class StatementControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    public static DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("src/test/test-compose.yaml"));

    @Test
    void sendOffers() {

    }

    @Test
    void selectOffer() {
    }
}