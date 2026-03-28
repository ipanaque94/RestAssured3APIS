package APITests.books;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;

import static io.restassured.RestAssured.given;

@Epic("Simple Books API")
@Feature("Contract")
@Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
public class BooksSchemaValidation extends BaseBooksTest {

    private static final Logger log = LoggerFactory.getLogger(BooksSchemaValidation.class);

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books — el Schema JSON debe coincidir con books-schemas.json")
    @Story("Validación del contrato")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Contract") })
    @Test(groups = { "Books", "Contract", "Smoke", "Regression" })
    public void validarSchemaDeBooksEsCorrecto() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que el esquema JSON del endpoint /books coincide con el contrato definido"));
        log.info("Iniciando test: validarSchemaDeBooksEsCorrecto");
        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .log().all()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/books-schemas.json"));
        log.info("Test completado exitosamente");
    }
}