package APITests.books;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import APITests.baseUrl.BaseBooksTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class BooksTest extends BaseBooksTest {

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /books debe retornar status 200")
    @Story("Listado de libros")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Books", "Smoke", "Regression" })
    public void validarStatusCode200Books() {
        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    @Severity(SeverityLevel.MINOR)
    @Description("GET /books debe responder en menos de 2000 ms")
    @Story("Performance")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Books", "Performance" })
    public void validarTiempoDeRespuesta() {
        given()
                .spec(spec)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .time(lessThan(2000L));
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("GET /books retorna la lista completa e imprime en consola")
    @Story("Listado de libros")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Books", "Regression" })
    public void listarLibrosConToken() {
        Response response = given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Lista de libros: " + response.asString());
    }

    @DataProvider(name = "librosValidos")
    public Object[][] librosValidos() {
        return new Object[][] {
                { 1 },
                { 2 },
                { 3 }
        };
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books/{id} debe retornar 200 y el id correcto para libros existentes")
    @Story("Búsqueda por ID")
    @Owner("Enoc Ipanaque")
    @Test(dataProvider = "librosValidos", groups = { "Books", "Regression" })
    public void validarLibroPorId(int bookId) {
        given()
                .spec(spec)
                .when()
                .get("/books/" + bookId)
                .then()
                .statusCode(200)
                .body("id", equalTo(bookId))
                .log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /books/999 debe retornar 404 cuando el libro no existe")
    @Story("Manejo de errores")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Books", "Negative" })
    public void validarStatusCode404LibroNoExiste() {
        given()
                .spec(spec)
                .when()
                .get("/books/999")
                .then()
                .log().all()
                .assertThat()
                .statusCode(404);
    }
}