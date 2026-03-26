package APITests.auth;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import APITests.baseUrl.BaseBooksTest;
import APITests.utils.Config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthTest extends BaseBooksTest {

    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api-clients sin cuerpo debe retornar 400 con mensaje de nombre inválido")
    @Story("Validación de campos obligatorios")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Auth", "Negative" })
    public void validarCuerpoVacioRetorna400() {
        given()
                .header("Content-Type", "application/json")
                .body("{}")
                .when()
                .post("/api-clients")
                .then()
                .assertThat()
                .statusCode(400)
                .body("error", equalTo("Invalid or missing client name."))
                .log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /api-clients solo con clientName debe retornar 400 por email faltante")
    @Story("Validación de campos obligatorios")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Auth", "Negative" })
    public void validarSoloNombreRetorna400() {
        given()
                .header("Content-Type", "application/json")
                .body("{\"clientName\": \"Postman\"}")
                .when()
                .post("/api-clients")
                .then()
                .assertThat()
                .statusCode(400)
                .body("error", equalTo("Invalid or missing client email."))
                .log().all();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("POST /api-clients con nombre y email válidos debe retornar 201 y un accessToken")
    @Story("Registro exitoso y obtención de token")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Auth", "Smoke", "Regression" })
    public void registroExitosoRetorna201ConToken() {
        given()
                .spec(spec)
                .body("{\"clientName\": \"Postman\", \"clientEmail\": \"" + generateRandomEmail() + "\"}")
                .when()
                .post("/api-clients")
                .then()
                .statusCode(201)
                .body("accessToken", notNullValue())
                .log().all();
    }
}