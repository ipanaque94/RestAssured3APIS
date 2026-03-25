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

        System.setProperty("java.net.useSystemProxies", "false");
        System.setProperty("socksProxyHost", "");
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");

        RestAssured.baseURI = APITests.utils.Config.get("rick.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        spec = new RequestSpecBuilder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
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