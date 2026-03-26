package APITests.rickandmorty;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RickAndMortyTest {

    protected static RequestSpecification spec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = APITests.utils.Config.get("rick.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        spec = new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .build();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /character/1 debe retornar 200 y los datos de Rick Sanchez correctos")
    @Story("Validación de personajes")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "RickAndMorty", "Smoke" })
    public void testGetPersonajeRick() {
        given()
                .spec(spec)
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
                .spec(spec)
                .when()
                .get("/character/1")
                .then()
                .statusCode(200)
                .body("location.name", notNullValue())
                .body("location.url", notNullValue())
                .body("origin.name", equalTo("Earth (C-137)"));
    }
}