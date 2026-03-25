package APITests.books;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class IdempotenciaTest extends BaseBooksTest {

        @Severity(SeverityLevel.CRITICAL)
        @Description("PATCH /orders/{id} ejecutado dos veces con el mismo cuerpo debe retornar 204 ambas veces")
        @Story("Idempotencia HTTP")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "Orders", "Idempotencia", "Regression" })
        public void validarIdempotenciaPATCH() {
                String orderId = crearOrden(token);
                System.out.println("Order creada para PATCH: " + orderId);

                String cuerpoActualizacion = "{\"customerName\": \"Nuevo Nombre\"}";

                Response primerPATCH = given()
                                .spec(spec)
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoActualizacion)
                                .when()
                                .patch("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();

                // Segunda actualización
                Response segundoPATCH = given()
                                .spec(spec)
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoActualizacion)
                                .when()
                                .patch("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();

                Assert.assertEquals(primerPATCH.getStatusCode(), segundoPATCH.getStatusCode(),
                                "Ambas respuestas PATCH deben tener el mismo status code");
                Assert.assertEquals(primerPATCH.getTime(), segundoPATCH.getTime(), 1000,
                                "Los tiempos de respuesta no deben diferir más de 1000ms");
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("DELETE /orders/{id}: primera llamada retorna 204, segunda retorna 404 (recurso ya eliminado)")
        @Story("Idempotencia HTTP")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "Orders", "Idempotencia", "Regression" })
        public void validarIdempotenciaDELETE() {
                String orderId = crearOrden(token);
                System.out.println("Order creada para DELETE: " + orderId);

                Response primerDelete = given()
                                .spec(spec)
                                .header("Authorization", "Bearer " + token)
                                .when()
                                .delete("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(204)
                                .extract().response();

                Response segundoDelete = given()
                                .spec(spec)
                                .header("Authorization", "Bearer " + token)
                                .when()
                                .delete("/orders/" + orderId)
                                .then()
                                .assertThat()
                                .statusCode(404)
                                .extract().response();

                Assert.assertEquals(primerDelete.getStatusCode(), 204,
                                "La primera eliminación debe retornar 204");
                Assert.assertEquals(segundoDelete.getStatusCode(), 404,
                                "La segunda eliminación debe retornar 404 (recurso ya eliminado)");
        }

        private String crearOrden(String token) {
                String cuerpoOrden = "{\"bookId\": 1, \"customerName\": \"Enoc\"}";

                return given()
                                .spec(spec)
                                .header("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + token)
                                .body(cuerpoOrden)
                                .when()
                                .post("/orders")
                                .then()
                                .assertThat()
                                .statusCode(201)
                                .extract()
                                .response()
                                .jsonPath()
                                .getString("orderId");
        }
}