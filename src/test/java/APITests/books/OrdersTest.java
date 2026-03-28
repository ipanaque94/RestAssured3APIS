package APITests.books;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;
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
        given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .when().get("/orders")
                .then().statusCode(200).log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token válido y cuerpo correcto debe retornar 201")
    @Story("Creación de órdenes")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Regression" })
    public void crearOrden() {
        OrderResponse resp = given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .body(new Order(1, "jhon"))
                .when().post("/orders")
                .then().statusCode(201).log().all()
                .extract().as(OrderResponse.class);
        System.out.println("Order ID: " + resp.getOrderId());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token válido pero cuerpo vacío debe retornar 400")
    @Story("Manejo de errores")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Negative" })
    public void validarStatusCode400CuandoCuerpoEsInvalido() {
        given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .body("{}")
                .when().post("/orders")
                .then().statusCode(400).log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token inválido debe retornar 401")
    @Story("Seguridad y autenticación")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Security", "Negative" })
    public void validarStatusCode401TokenInvalidoEnPost() {
        given().spec(spec)
                .header("Authorization", "Bearer TokenInvalido")
                .body("{\"bookId\":1,\"customerName\":\"Test\"}")
                .when().post("/orders")
                .then().statusCode(401).log().all();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /orders con token inválido debe retornar 401")
    @Story("Seguridad y autenticación")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "Orders", "Security", "Negative" })
    public void validarStatusCode401TokenInvalidoEnGet() {
        given().spec(spec)
                .header("Authorization", "Bearer TokenInvalido")
                .when().get("/orders")
                .then().statusCode(401).log().all();
    }
}