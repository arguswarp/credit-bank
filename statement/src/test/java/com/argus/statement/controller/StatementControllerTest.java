package com.argus.statement.controller;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * StatementControllerTest
 *
 * @author Maxim Chistyakov
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
//@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper objectMapper;

    private LoanStatementRequestDto request;

    @Container
    public static DockerComposeContainer<?> environment =
            new DockerComposeContainer<>(new File("src/test/resources/test-compose.yaml"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        environment.withExposedService("deal", 8081);
        environment.start();

        registry.add("spring.cloud.openfeign.client.config.deal-client.url",
                () -> environment.getServiceHost("deal", 8081) + ":"
                      + environment.getServicePort("deal", 8081) + "/deal/");
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        request = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(30000))
                .term(36)
                .firstName("John")
                .lastName("Doe")
                .email("john@mail.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    @AfterAll
    static void tearDown() {
        environment.stop();
    }

    @Test
    void sendOffers() {
        postStatement(request)
                .then()
                .statusCode(200)
                .body("statementId", Matchers.hasSize(4))
                .body("statementId", Matchers.everyItem(Matchers.notNullValue()))
                .body("requestedAmount[0]", Matchers.is(30000))
                .body("term[0]", Matchers.is(36));
    }

    @Test
    void whenRequestWithValidationErrors_thenThrowsException() {
        request.setFirstName("123");
        request.setAmount(BigDecimal.valueOf(200));

        postStatement(request)
                .then()
                .statusCode(400);
    }

    @Test
    void selectOffer() throws JsonProcessingException {
        ExtractableResponse <Response> offers = postStatement(request).then().statusCode(200).extract();
        List<LoanOfferDto> offersList = objectMapper.readValue(offers.body().asString(), new TypeReference<>() {});

        offersList.forEach(loanOfferDto -> {
            assertNotNull(loanOfferDto);
            assertNotNull(loanOfferDto.getStatementId());
        });

        given()
                .contentType(ContentType.JSON)
                .body(offersList.get(0))
                .when()
                .post("/statement/offer")
                .then()
                .statusCode(204);
    }

    private Response postStatement(Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/statement");
    }
}