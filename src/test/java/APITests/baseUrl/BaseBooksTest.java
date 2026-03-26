package APITests.baseUrl;

import static io.restassured.RestAssured.given;
//import java.util.Random;
import org.testng.annotations.BeforeClass;
import APITests.utils.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseBooksTest {

    protected String token;

    protected RequestSpecification spec;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = Config.get("books.api.url");
        RestAssured.port = 443;
        RestAssured.useRelaxedHTTPSValidation();

        spec = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        token = obtenerToken();
    }

    //
    protected String obtenerToken() {
        for (int i = 0; i < 3; i++) {
            String email = generateRandomEmail();

            var response = given()
                    .spec(spec)
                    .log().uri()
                    .body("{\"clientName\": \"Enoc\", \"clientEmail\": \"" + email + "\"}")
                    .when()
                    .post("/api-clients");
            System.out.println("Respuesta: " + response.asString());

            if (response.statusCode() == 201) {
                return response.path("accessToken");
            } else {
                System.out.println("Intento " + (i + 1) + " falló con status: " + response.statusCode());
            }
        }
        throw new RuntimeException("No se pudo obtener token después de varios intentos");
    }

    protected String generateRandomEmail() {
        return "user" + System.currentTimeMillis() + "@mail.com";
    }
}