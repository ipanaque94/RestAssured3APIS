package APITests.baseUrl;

import static io.restassured.RestAssured.given;
import org.testng.annotations.BeforeClass;
import APITests.utils.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseBooksTest {

    protected static String token;
    protected static RequestSpecification spec;
    private static boolean initialized = false;

    @BeforeClass(alwaysRun = true)
    public synchronized void setup() {
        if (initialized)
            return; // ya listo — no tocar nada

        String url = Config.get("books.api.url");
        System.out.println("=== BaseBooksTest.setup | url=" + url + " ===");

        RestAssured.baseURI = url;
        RestAssured.useRelaxedHTTPSValidation();

        spec = new RequestSpecBuilder()
                .setBaseUri(url) // ✅ baseUri dentro del spec también
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        token = obtenerToken();
        initialized = true;
    }

    private String obtenerToken() {
        for (int i = 0; i < 3; i++) {
            String email = generateRandomEmail();
            var response = given()
                    .spec(spec)
                    .body("{\"clientName\":\"Enoc\",\"clientEmail\":\"" + email + "\"}")
                    .when()
                    .post("/api-clients");

            System.out.println("obtenerToken intento " + (i + 1)
                    + " | " + response.statusCode()
                    + " | " + response.asString());

            if (response.statusCode() == 201)
                return response.path("accessToken");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            }
        }
        throw new RuntimeException("No se pudo obtener token después de 3 intentos");
    }

    protected String generateRandomEmail() {
        return "user" + System.nanoTime() + "@mail.com";
    }
}