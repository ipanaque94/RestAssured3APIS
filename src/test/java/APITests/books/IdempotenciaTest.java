package APITests.books;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;

import static io.restassured.RestAssured.given;

@Epic("Simple Books API")
@Feature("Idempotencia")
@Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
public class IdempotenciaTest extends BaseBooksTest {

        private static final Logger log = LoggerFactory.getLogger(IdempotenciaTest.class);

        @Severity(SeverityLevel.CRITICAL)
        @Description("PATCH /orders/{id} ejecutado dos veces debe retornar 204 ambas veces")
        @Story("Idempotencia HTTP")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("Orders"), @Tag("Idempotencia") })
        @Test(groups = { "Orders", "Idempotencia", "Regression" })
        public void validarIdempotenciaPATCH() {
                Allure.getLifecycle().updateTestCase(tc -> tc.setName(
                                "Valida que PATCH /orders/{id} ejecutado dos veces retorna 204 en ambas llamadas"));
                log.info("Iniciando test: validarIdempotenciaPATCH");
                String orderId = crearOrden(token);
                String cuerpo = "{\"customerName\": \"Nuevo Nombre\"}";

                Response primero = given().spec(spec)
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpo).when().patch("/orders/" + orderId)
                                .then().statusCode(204).extract().response();

                Response segundo = given().spec(spec)
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpo).when().patch("/orders/" + orderId)
                                .then().statusCode(204).extract().response();

                Assert.assertEquals(primero.getStatusCode(), segundo.getStatusCode());
                log.info("Test completado exitosamente");
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("DELETE /orders/{id}: primera llamada retorna 204, segunda retorna 404")
        @Story("Idempotencia HTTP")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("Orders"), @Tag("Idempotencia") })
        @Test(groups = { "Orders", "Idempotencia", "Regression" })
        public void validarIdempotenciaDELETE() {
                Allure.getLifecycle().updateTestCase(tc -> tc
                                .setName("Valida que DELETE /orders/{id} retorna 204 la primera vez y 404 la segunda"));
                log.info("Iniciando test: validarIdempotenciaDELETE");
                String orderId = crearOrden(token);

                given().spec(spec).header("Authorization", "Bearer " + token)
                                .when().delete("/orders/" + orderId)
                                .then().statusCode(204);

                given().spec(spec).header("Authorization", "Bearer " + token)
                                .when().delete("/orders/" + orderId)
                                .then().statusCode(404);

                log.info("Test completado exitosamente");
        }

        private String crearOrden(String token) {
                return given().spec(spec)
                                .header("Authorization", "Bearer " + token)
                                .body("{\"bookId\": 1, \"customerName\": \"Enoc\"}")
                                .when().post("/orders")
                                .then().statusCode(201)
                                .extract().response().jsonPath().getString("orderId");
        }
}