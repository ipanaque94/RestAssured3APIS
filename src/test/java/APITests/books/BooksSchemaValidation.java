package APITests.books;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class BooksSchemaValidation extends BaseBooksTest {

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books — el Schema JSON del endpoint debe coincidir con books-schemas.json")
    @Story("Validación del contrato")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Books", "Contract", "Smoke", "Regression" })
    public void validarSchemaDeBooksEsCorrecto() {
        Allure.getLifecycle().updateTestCase(tc -> tc.setName("El esquema del endpoint /books es el correcto"));

        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/books-schemas.json"));
    }
}