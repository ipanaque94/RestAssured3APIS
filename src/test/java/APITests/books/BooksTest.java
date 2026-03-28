package APITests.books;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

@Epic("Simple Books API")
@Feature("Books")
@Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
public class BooksTest extends BaseBooksTest {

    private static final Logger log = LoggerFactory.getLogger(BooksTest.class);

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /books debe retornar status 200")
    @Story("GET Books")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Test Positivo") })
    @Test(groups = { "Books", "Smoke", "Regression" })
    public void validarStatusCode200Books() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que al enviar un GET al endpoint /books obtenemos un código de estado 200"));
        log.info("Iniciando test: validarStatusCode200Books");
        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .log().all()
                .statusCode(200);
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.MINOR)
    @Description("GET /books debe responder en menos de 2000 ms")
    @Story("Performance Books")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Performance") })
    @Test(groups = { "Books", "Performance" })
    public void validarTiempoDeRespuesta() {
        Allure.getLifecycle()
                .updateTestCase(tc -> tc.setName("Valida que el endpoint /books responde en menos de 2000 ms"));
        log.info("Iniciando test: validarTiempoDeRespuesta");
        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .time(lessThan(2000L));
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("GET /books retorna la lista completa de libros")
    @Story("GET Books")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Test Positivo") })
    @Test(groups = { "Books", "Regression" })
    public void listarLibrosConToken() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que GET /books con token válido retorna la lista completa de libros"));
        log.info("Iniciando test: listarLibrosConToken");
        Response response = given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();
        log.info("Test completado exitosamente — libros: {}", response.asString());
    }

    @DataProvider(name = "librosValidos")
    public Object[][] librosValidos() {
        return new Object[][] { { 1 }, { 2 }, { 3 } };
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books/{id} debe retornar 200 y el id correcto")
    @Story("GET Books por ID")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Test Positivo") })
    @Test(dataProvider = "librosValidos", groups = { "Books", "Regression" })
    public void validarLibroPorId(int bookId) {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que GET /books/" + bookId + " retorna el libro con id correcto"));
        log.info("Iniciando test: validarLibroPorId — bookId={}", bookId);
        given()
                .spec(spec)
                .when()
                .get("/books/" + bookId)
                .then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books/999 debe retornar 404 cuando el libro no existe")
    @Story("GET Books — Manejo de errores")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Books"), @Tag("Test Negativo") })
    @Test(groups = { "Books", "Negative" })
    public void validarStatusCode404LibroNoExiste() {
        Allure.getLifecycle()
                .updateTestCase(tc -> tc.setName("Valida que GET /books/999 retorna 404 cuando el libro no existe"));
        log.info("Iniciando test: validarStatusCode404LibroNoExiste");
        given()
                .spec(spec)
                .when()
                .get("/books/999")
                .then()
                .log().all()
                .statusCode(404);
        log.info("Test completado exitosamente");
    }
}