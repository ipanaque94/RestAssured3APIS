package APITests.baseUrl;

import static io.restassured.RestAssured.given;
//import java.util.Random;
import org.testng.annotations.BeforeClass;
import APITests.utils.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseBooksTest {

    protected static String token;

    protected static RequestSpecification spec;

    @BeforeClass(alwaysRun = true)
    public synchronized void setup() {

        if (spec == null) {
            RestAssured.baseURI = Config.get("books.api.url");
            RestAssured.useRelaxedHTTPSValidation();
            spec = new RequestSpecBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
        }
        if (token == null) {
            token = obtenerToken();
        }
    }

    private String obtenerToken() {
        for (int i = 0; i < 3; i++) {

            var response = given()
                    .spec(spec)
                    .body("{\"clientName\": \"Enoc\", \"clientEmail\": \"" + generateRandomEmail() + "\"}")
                    .when()
                    .post("/api-clients");

            System.out.println("obtenerToken intento " + (i + 1)
                    + " | status: " + response.statusCode()
                    + " | body: " + response.asString());

            if (response.statusCode() == 201) {
                return response.path("accessToken");
            }
            // Pausa entre intentos para evitar rate limit
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        throw new RuntimeException("No se pudo obtener token después de 3 intentos");
    }

    protected String generateRandomEmail() {
        return "user" + System.nanoTime() + "@mail.com";
    }
}