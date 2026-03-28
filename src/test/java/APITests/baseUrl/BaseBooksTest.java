package APITests.baseUrl;

import static io.restassured.RestAssured.given;
import org.testng.annotations.BeforeClass;
import APITests.utils.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseBooksTest {

    protected String token = "";
    protected RequestSpecification spec;

    @BeforeClass(alwaysRun = true, groups = { "Books", "Orders", "Auth" })
    public void setup() {
        RestAssured.baseURI = Config.get("books.api.url");
        RestAssured.useRelaxedHTTPSValidation();

        spec = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try {
            token = obtenerToken();
        } catch (Exception e) {
            System.out.println("WARNING: Token no disponible: " + e.getMessage());
        }
    }

    private String obtenerToken() {
        for (int i = 0; i < 3; i++) {
            String email = generateRandomEmail();
            var response = given()
                    .spec(spec)
                    .body("{\"clientName\": \"Enoc\", \"clientEmail\": \"" + email + "\"}")
                    .when()
                    .post("/api-clients");

            System.out.println("Token intento " + (i + 1) + " | status: " + response.statusCode());

            if (response.statusCode() == 201) {
                return response.path("accessToken");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        throw new RuntimeException("No se pudo obtener token");
    }

    protected String generateRandomEmail() {
        return "user" + System.nanoTime() + "@mail.com";
    }
}