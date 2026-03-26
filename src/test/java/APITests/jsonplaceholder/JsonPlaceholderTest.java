package APITests.jsonplaceholder;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import APITests.baseUrl.BaseJsonPlaceHolder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholderTest extends BaseJsonPlaceHolder {

        @Severity(SeverityLevel.BLOCKER)
        @Description("GET /posts/1 debe retornar 200, userId=1 y title no nulo")
        @Story("Operaciones GET")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "JsonPlaceholder", "Smoke" })
        public void testGetPost() {
                given()
                                .spec(spec)
                                .when()
                                .get("/posts/1")
                                .then()
                                .statusCode(200)
                                .body("userId", equalTo(1))
                                .body("title", notNullValue());
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("GET /users/1 debe retornar campos name, username, email y coordenada lat correctos")
        @Story("Operaciones GET")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testGetUserYValidarCampos() {
                Response response = given()
                                .spec(spec)
                                .when()
                                .get("/users/1")
                                .then()
                                .statusCode(200)
                                .extract()
                                .response();

                System.out.println("Name: " + response.path("name"));
                System.out.println("Username: " + response.path("username"));
                System.out.println("Email: " + response.path("email"));
                System.out.println("Lat: " + response.path("address.geo.lat"));

                response.then()
                                .body("name", equalTo("Leanne Graham"))
                                .body("username", equalTo("Bret"))
                                .body("email", equalTo("Sincere@april.biz"))
                                .body("address.geo.lat", equalTo("-37.3159"));
        }

        @Severity(SeverityLevel.CRITICAL)
        @Description("POST /posts con cuerpo válido debe retornar 201, title correcto e id no nulo")
        @Story("Operaciones POST")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testCrearPost() {
                String body = """
                                {
                                    "title": "Mi primer post QA",
                                    "body": "Aprendiendo Rest Assured",
                                    "userId": 1
                                }
                                """;

                given()
                                .spec(spec)
                                .body(body)
                                .when()
                                .post("/posts")
                                .then()
                                .statusCode(201)
                                .body("title", equalTo("Mi primer post QA"))
                                .body("id", notNullValue());
        }

        @Severity(SeverityLevel.NORMAL)
        @Description("PUT /posts/1 debe retornar 200 y el título actualizado")
        @Story("Operaciones PUT")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testActualizarPost() {
                String body = """
                                {
                                    "id": 1,
                                    "title": "Post actualizado",
                                    "body": "Contenido actualizado",
                                    "userId": 1
                                }
                                """;

                given()
                                .spec(spec)
                                .body(body)
                                .when()
                                .put("/posts/1")
                                .then()
                                .statusCode(200)
                                .body("title", equalTo("Post actualizado"));
        }

        @Severity(SeverityLevel.NORMAL)
        @Description("DELETE /posts/1 debe retornar 200")
        @Story("Operaciones DELETE")
        @Owner("Enoc Ipanaque")
        @Test(groups = { "JsonPlaceholder", "Regression" })
        public void testEliminarPost() {
                given()
                                .spec(spec)
                                .when()
                                .delete("/posts/1")
                                .then()
                                .statusCode(200);
        }
}