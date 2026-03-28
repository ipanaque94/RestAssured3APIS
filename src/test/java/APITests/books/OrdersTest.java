package APITests.books;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseBooksTest;
import APITests.utils.Order;
import APITests.utils.OrderResponse;

import static io.restassured.RestAssured.given;

@Epic("Simple Books API")
@Feature("Orders")
@Link("https://github.com/vdespa/introduction-to-postman-course/blob/main/simple-books-api.md")
public class OrdersTest extends BaseBooksTest {

    private static final Logger log = LoggerFactory.getLogger(OrdersTest.class);

    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /orders con token válido debe retornar 200")
    @Story("GET Orders")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Orders"), @Tag("Test Positivo") })
    @Test(groups = { "Orders", "Smoke", "Regression" })
    public void listarOrdenes() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que GET /orders con token válido retorna un código de estado 200"));
        log.info("Iniciando test: listarOrdenes");
        given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .when().get("/orders")
                .then().statusCode(200).log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token válido y cuerpo correcto debe retornar 201")
    @Story("POST Orders")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Orders"), @Tag("Test Positivo") })
    @Test(groups = { "Orders", "Regression" })
    public void crearOrden() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /orders con datos válidos crea una orden y retorna 201"));
        log.info("Iniciando test: crearOrden");
        OrderResponse resp = given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .body(new Order(1, "jhon"))
                .when().post("/orders")
                .then().statusCode(201).log().all()
                .extract().as(OrderResponse.class);
        log.info("Test completado — Order ID: {}", resp.getOrderId());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con cuerpo vacío debe retornar 400")
    @Story("POST Orders — Manejo de errores")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Orders"), @Tag("Test Negativo") })
    @Test(groups = { "Orders", "Negative" })
    public void validarStatusCode400CuandoCuerpoEsInvalido() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /orders con cuerpo vacío retorna un código de estado 400"));
        log.info("Iniciando test: validarStatusCode400CuandoCuerpoEsInvalido");
        given().spec(spec)
                .header("Authorization", "Bearer " + token)
                .body("{}")
                .when().post("/orders")
                .then().statusCode(400).log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /orders con token inválido debe retornar 401")
    @Story("POST Orders — Seguridad")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Orders"), @Tag("Seguridad") })
    @Test(groups = { "Orders", "Security", "Negative" })
    public void validarStatusCode401TokenInvalidoEnPost() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que POST /orders con token inválido retorna un código de estado 401"));
        log.info("Iniciando test: validarStatusCode401TokenInvalidoEnPost");
        given().spec(spec)
                .header("Authorization", "Bearer TokenInvalido")
                .body("{\"bookId\":1,\"customerName\":\"Test\"}")
                .when().post("/orders")
                .then().statusCode(401).log().all();
        log.info("Test completado exitosamente");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /orders con token inválido debe retornar 401")
    @Story("GET Orders — Seguridad")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("Orders"), @Tag("Seguridad") })
    @Test(groups = { "Orders", "Security", "Negative" })
    public void validarStatusCode401TokenInvalidoEnGet() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que GET /orders con token inválido retorna un código de estado 401"));
        log.info("Iniciando test: validarStatusCode401TokenInvalidoEnGet");
        given().spec(spec)
                .header("Authorization", "Bearer TokenInvalido")
                .when().get("/orders")
                .then().statusCode(401).log().all();
        log.info("Test completado exitosamente");
    }
}