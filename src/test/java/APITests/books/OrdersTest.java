package APITests.books;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import APITests.utils.Order;
import APITests.utils.OrderResponse;

import static io.restassured.RestAssured.given;

public class OrdersTest extends BaseBooksTest {

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /orders con token válido debe retornar 200")
    @Story("Listado de órdenes")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Smoke", "Regression" })
    public void listarOrdenes() {
        given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token válido y cuerpo correcto debe retornar 201")
    @Story("Creación de órdenes")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Regression" })
    public void crearOrden() {
        Order nuevaOrden = new Order(1, "jhon");

        OrderResponse respuesta = given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .body(nuevaOrden)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .log().all()
                .extract()
                .as(OrderResponse.class);

        System.out.println("Order ID creado: " + respuesta.getOrderId());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token válido pero cuerpo vacío debe retornar 400")
    @Story("Manejo de errores")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Negative" })
    public void validarStatusCode400CuandoCuerpoEsInvalido() {
        given()
                .spec(spec)
                .header("Authorization", "Bearer " + token)
                .body("{}")
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token inválido debe retornar 401")
    @Story("Seguridad y autenticación")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Security", "Negative" })
    public void validarStatusCode401TokenInvalidoEnPost() {
        given()
                .spec(spec)
                .header("Authorization", "Bearer InvalidoToken")
                .body("{\"bookId\": 1, \"customerName\": \"Test\"}")
                .when()
                .post("/orders")
                .then()
                .statusCode(401)
                .log().all();
    }

    @DataProvider(name = "tokensInvalidos")
    public Object[][] tokensInvalidos() {
        return new Object[][] {
                { "tokenFalso123", 401 },
                { "invalidoToken", 401 },
                { "", 401}
        }; 
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /orders con distintos tokens inválidos siempre debe retornar 401")
    @Story("Seguridad y autenticación")
    @Owner("Enoc Ipanaque")
    @Test(dataProvider = "tokensInvalidos", groups = { "Orders", "Security", "Negative" })
    public void validarTokensInvalidosEnGet(String tokenInvalido, int statusEsperado) {
        given()
                .spec(spec)
                .header("Authorization", "Bearer " + tokenInvalido)
                .when()
                .get("/orders")
                .then()
                .statusCode(statusEsperado)
                .log().all();
    }
}