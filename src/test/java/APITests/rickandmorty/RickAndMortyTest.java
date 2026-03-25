package APITests.rickandmorty;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import APITests.utils.Config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RickAndMortyTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.get("rick.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .build();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /character/1 debe retornar 200 y los datos de Rick Sanchez correctos")
    @Story("Validación de personajes")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "RickAndMorty", "Smoke" })
    public void testGetPersonajeRick() {
        given()

                .when()
                .get("/character/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Rick Sanchez"))
                .body("status", equalTo("Alive"))
                .body("species", equalTo("Human"))
                .body("gender", equalTo("Male"));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /character/1 debe retornar campos anidados location, origin y episode correctos")
    @Story("Validación de JsonPath anidado")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "RickAndMorty", "Regression" })
    public void testValidarCamposAnidadosConJsonPath() {
        given()
                .when()
                .get("/character/1")
                .then()
                .statusCode(200)
                // Objeto location
                .body("location.name", notNullValue())
                .body("location.url", notNullValue())
                // Objeto origin
                .body("origin.name", equalTo("Earth (C-137)"));
    }
}