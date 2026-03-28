package APITests.jsonplaceholder;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseJsonPlaceHolder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("JSONPlaceholder API")
@Feature("Posts")
@Link("https://jsonplaceholder.typicode.com")
public class JsonPlaceholderTest extends BaseJsonPlaceHolder {

        private static final Logger log = LoggerFactory.getLogger(JsonPlaceholderTest.class);

        @Severity(SeverityLevel.BLOCKER)
        @Description("GET /posts/1 debe retornar 200, userId=1 y title no nulo")
        @Story("GET Posts")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("JsonPlaceholder"), @Tag("Test Positivo") })
        @Test(groups = { "JsonPlaceholder", "Smoke" })
        public void testGetPost() {
                Allure.getLifecycle().updateTestCase(
                                tc -> tc.setName("Valida que GET /posts/1 retorna 200 con userId=1 y title no nulo"));
                log.info("Iniciando test: testGetPost");
                given().spec(spec).when().get("/posts/1")
                                .then().statusCode(200)
                                .body("userId", equalTo(1))
                                .body("title", notNullValue());
                log.info("Test completado exitosamente");
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("GET /users/1 debe retornar campos name, username, email y lat correctos")
        @Story("GET Users")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("JsonPlaceholder"), @Tag("Test Positivo") })
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testGetUserYValidarCampos() {
                Allure.getLifecycle().updateTestCase(tc -> tc.setName(
                                "Valida que GET /users/1 retorna los campos name, username, email y coordenada lat correctos"));
                log.info("Iniciando test: testGetUserYValidarCampos");
                Response response = given().spec(spec).when().get("/users/1")
                                .then().statusCode(200).extract().response();
                response.then()
                                .body("name", equalTo("Leanne Graham"))
                                .body("username", equalTo("Bret"))
                                .body("email", equalTo("Sincere@april.biz"))
                                .body("address.geo.lat", equalTo("-37.3159"));
                log.info("Test completado exitosamente");
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("POST /posts con cuerpo válido debe retornar 201 y title correcto")
        @Story("POST Posts")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("JsonPlaceholder"), @Tag("Test Positivo") })
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testCrearPost() {
                Allure.getLifecycle().updateTestCase(tc -> tc
                                .setName("Valida que POST /posts con datos válidos retorna 201 y el título correcto"));
                log.info("Iniciando test: testCrearPost");
                given().spec(spec)
                                .body("{\"title\":\"Mi primer post QA\",\"body\":\"Aprendiendo Rest Assured\",\"userId\":1}")
                                .when().post("/posts")
                                .then().statusCode(201)
                                .body("title", equalTo("Mi primer post QA"))
                                .body("id", notNullValue());
                log.info("Test completado exitosamente");
        }

        @Severity(SeverityLevel.NORMAL)
        @Description("PUT /posts/1 debe retornar 200 y el título actualizado")
        @Story("PUT Posts")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("JsonPlaceholder"), @Tag("Test Positivo") })
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testActualizarPost() {
                Allure.getLifecycle().updateTestCase(tc -> tc.setName(
                                "Valida que PUT /posts/1 actualiza el recurso y retorna 200 con el título correcto"));
                log.info("Iniciando test: testActualizarPost");
                given().spec(spec)
                                .body("{\"id\":1,\"title\":\"Post actualizado\",\"body\":\"Contenido actualizado\",\"userId\":1}")
                                .when().put("/posts/1")
                                .then().statusCode(200)
                                .body("title", equalTo("Post actualizado"));
                log.info("Test completado exitosamente");
        }

        @Severity(SeverityLevel.NORMAL)
        @Description("DELETE /posts/1 debe retornar 200")
        @Story("DELETE Posts")
        @Owner("Enoc Ipanaque")
        @Tags({ @Tag("JsonPlaceholder"), @Tag("Test Negativo") })
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testEliminarPost() {
                Allure.getLifecycle().updateTestCase(
                                tc -> tc.setName("Valida que DELETE /posts/1 elimina el recurso y retorna 200"));
                log.info("Iniciando test: testEliminarPost");
                given().spec(spec).when().delete("/posts/1").then().statusCode(200);
                log.info("Test completado exitosamente");
        }
}