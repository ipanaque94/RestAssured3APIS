package APITests.rickandmorty;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseRickMorty;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Rick and Morty API")
@Feature("Characters")
@Link("https://rickandmortyapi.com/documentation")
public class RickAndMortyTest extends BaseRickMorty {

    private static final Logger log = LoggerFactory.getLogger(RickAndMortyTest.class);

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /character/1 debe retornar 200 y los datos de Rick Sanchez correctos")
    @Story("GET Character")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("RickAndMorty"), @Tag("Test Positivo") })
    @Test(groups = { "RickAndMorty", "Smoke" })
    public void testGetPersonajeRick() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que GET /character/1 retorna los datos correctos de Rick Sanchez"));
        log.info("Iniciando test: testGetPersonajeRick");
        given().spec(spec).when().get("/character/1")
                .then().statusCode(200)
                .body("name", equalTo("Rick Sanchez"))
                .body("status", equalTo("Alive"))
                .body("species", equalTo("Human"))
                .body("gender", equalTo("Male"));
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /character/1 debe retornar campos anidados location y origin correctos")
    @Story("GET Character — JsonPath anidado")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("RickAndMorty"), @Tag("Test Positivo") })
    @Test(groups = { "RickAndMorty", "Regression" })
    public void testValidarCamposAnidadosConJsonPath() {
        Allure.getLifecycle().updateTestCase(tc -> tc
                .setName("Valida que GET /character/1 retorna los campos anidados location y origin correctos"));
        log.info("Iniciando test: testValidarCamposAnidadosConJsonPath");
        given().spec(spec).when().get("/character/1")
                .then().statusCode(200)
                .body("location.name", notNullValue())
                .body("location.url", notNullValue())
                .body("origin.name", equalTo("Earth (C-137)"));
        log.info("Test completado exitosamente");
    }
}