package APITests.books;

import static io.restassured.RestAssured.given;
import java.util.Random;
import org.testng.annotations.BeforeClass;
import APITests.utils.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseBooksTest {

    protected String token;

    protected static RequestSpecification spec = new RequestSpecBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Config.get("books.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        token = obtenerToken();
    }

    protected String obtenerToken() {
        String email = generateRandomEmail();
        return given()
                .spec(spec)
                .body("{\"clientName\": \"Enoc\", \"clientEmail\": \"" + email + "\"}")
                .when()
                .post("/api-clients")
                .then()
                .statusCode(201)
                .extract()
                .path("accessToken");
    }

    protected String generateRandomEmail() {
        return "user" + new Random().nextInt(999999) + "@example.com";
    }
}