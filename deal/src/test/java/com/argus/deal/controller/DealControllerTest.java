package com.argus.deal.controller;

import com.argus.deal.dto.EmploymentDto;
import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Credit;
import com.argus.deal.entity.Passport;
import com.argus.deal.entity.Statement;
import com.argus.deal.model.enums.*;
import com.argus.deal.repository.ClientRepository;
import com.argus.deal.repository.CreditRepository;
import com.argus.deal.repository.StatementRepository;
import com.argus.deal.service.StatementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * ${NAME}.
 *
 * @author Maxim Chistyakov
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test")
class DealControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private StatementService statementService;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client client;

    @Container
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension
            .newInstance()
            .options(wireMockConfig().port(8090))
            .configureStaticDsl(true)
            .build();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        statementRepository.deleteAll();
        clientRepository.deleteAll();
        creditRepository.deleteAll();

        client = Client.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@mail.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passport(Passport.builder()
                        .id(UUID.randomUUID())
                        .series("1234")
                        .number("123456")
                        .build())
                .build();
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("deal.rest-client.root-uri", wireMock::baseUrl);
    }

    @Test
    void sendOffers() throws JsonProcessingException {
        stubFor(WireMock.post("/offers")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock-responses/LoanOffers.json"))
        );

        ExtractableResponse<Response> offers = given()
                .contentType(ContentType.JSON)
                .body(LoanStatementRequestDto.builder()
                        .amount(BigDecimal.valueOf(30000))
                        .term(36)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@mail.com")
                        .birthdate(LocalDate.of(1990, 1, 1))
                        .passportSeries("1234")
                        .passportNumber("123456")
                        .build())
                .when()
                .post("/deal/statement")
                .then()
                .statusCode(200)
                .body("statementId", Matchers.hasSize(4))
                .body("statementId", Matchers.everyItem(Matchers.notNullValue()))
                .body("requestedAmount[0]", Matchers.is(30000))
                .body("term[0]", Matchers.is(36))
                .body("monthlyPayment[0]", Matchers.is(1039.96f))
                .extract();

        List<LoanOfferDto> offersList = objectMapper.readValue(offers.body().asString(), new TypeReference<>() {
        });
        Statement statement = statementService.get(offersList.get(0).getStatementId());
        assertNotNull(statement);
        assertNotNull(statement.getId());
    }

    @Test
    void selectOffer() {
        Statement statement = statementService.save(client);

        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(statement.getId())
                .requestedAmount(BigDecimal.valueOf(30000))
                .totalAmount(BigDecimal.valueOf(30000))
                .term(36)
                .monthlyPayment(BigDecimal.valueOf(1039.96))
                .rate(BigDecimal.valueOf(15))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(loanOfferDto)
                .when()
                .post("/deal/offer/select")
                .then()
                .statusCode(204);

        Statement statementUpdated = statementService.get(statement.getId());

        assertNotNull(statementUpdated);
        assertNotNull(statementUpdated.getAppliedOffer());
        assertEquals(loanOfferDto, statementUpdated.getAppliedOffer());

    }

    @Test
    void WhenSelectOfferAndGivenStatementWithStatusNotPreapproval_ThenStatus400() {
        Statement statement = statementService.save(client);
        statement.setStatus(Status.APPROVED);
        statementService.update(statement);

        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(statement.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(loanOfferDto)
                .when()
                .post("/deal/offer/select")
                .then()
                .statusCode(400);
    }

    @Test
    void calculate() {
        Statement statement = statementService.save(client);

        FinishRegistrationRequestDto finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .passportIssueBranch("some branch")
                .employment(EmploymentDto.builder()
                        .workExperienceCurrent(36)
                        .workExperienceTotal(360)
                        .position(Position.MANAGER)
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(BigDecimal.valueOf(100_000))
                        .employerINN("1234567890")
                        .build())
                .accountNumber("123456")
                .passportIssueDate(LocalDate.of(2000, 9, 12))
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .build();

        stubFor(WireMock.post("/calc")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock-responses/CreditDto.json"))
        );

        given()
                .contentType(ContentType.JSON)
                .body(finishRegistrationRequestDto)
                .when()
                .post("/deal/calculate/{statementId}", statement.getId())
                .then()
                .statusCode(204);

        Statement statementUpdated = statementService.get(statement.getId());

        assertNotNull(statementUpdated);
        assertNotNull(statementUpdated.getCredit());
        assertEquals(Status.CC_APPROVED, statementUpdated.getStatus());
        assertEquals(12, statementUpdated.getCredit().getPaymentSchedule().size());
    }

    @Test
    void WhenCalculateOfferForStatementWithCredit_ThenStatus400() {
        Statement statement = statementService.save(client);
        statement.setCredit(Credit.builder()
                .amount(BigDecimal.valueOf(30000))
                .psk(BigDecimal.TEN)
                .creditStatus(CreditStatus.CALCULATED)
                .rate(BigDecimal.TEN)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .monthlyPayment(BigDecimal.ONE)
                .paymentSchedule(List.of())
                .term(36)
                .build());
        statementService.update(statement);

        FinishRegistrationRequestDto finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .build();

        stubFor(WireMock.post("/calc")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock-responses/CreditDto.json"))
        );

        given()
                .contentType(ContentType.JSON)
                .body(finishRegistrationRequestDto)
                .when()
                .post("/deal/calculate/{statementId}", statement.getId())
                .then()
                .statusCode(400);
    }
}