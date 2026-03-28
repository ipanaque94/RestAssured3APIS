package APITests.auth;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Simple Books API")
@Feature("Autenticación")
@Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
public class AuthTest extends BaseBooksTest {

    private static final Logger log = LoggerFactory.getLogger(AuthTest.class);

    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api-clients sin cuerpo debe retornar 400")
    @Story("Registro de cliente")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Auth"), @Tag("Test Negativo") })
    @Test(groups = { "Auth", "Negative" })
    public void validarCuerpoVacioRetorna400() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /api-clients con cuerpo vacío retorna 400 por nombre faltante"));
        log.info("Iniciando test: validarCuerpoVacioRetorna400");
        given().spec(spec).body("{}")
                .when().post("/api-clients")
                .then().statusCode(400)
                .body("error", equalTo("Invalid or missing client name."))
                .log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api-clients solo con clientName debe retornar 400 por email faltante")
    @Story("Registro de cliente")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Auth"), @Tag("Test Negativo") })
    @Test(groups = { "Auth", "Negative" })
    public void validarSoloNombreRetorna400() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /api-clients sin email retorna 400 por email faltante"));
        log.info("Iniciando test: validarSoloNombreRetorna400");
        given().spec(spec).body("{\"clientName\": \"Postman\"}")
                .when().post("/api-clients")
                .then().statusCode(400)
                .body("error", equalTo("Invalid or missing client email."))
                .log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api-clients con datos válidos debe retornar 201 y un accessToken")
    @Story("Registro de cliente")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Auth"), @Tag("Test Positivo") })
    @Test(groups = { "Auth", "Smoke", "Regression" })
    public void registroExitosoRetorna201ConToken() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /api-clients con datos válidos retorna 201 y un accessToken"));
        log.info("Iniciando test: registroExitosoRetorna201ConToken");
        given().spec(spec)
                .body("{\"clientName\": \"Postman\", \"clientEmail\": \"" + generateRandomEmail() + "\"}")
                .when().post("/api-clients")
                .then().statusCode(201)
                .body("accessToken", notNullValue())
                .log().all();
        log.info("Test completado exitosamente");
    }
}