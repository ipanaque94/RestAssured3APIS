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
    protected static RequestSpecification spec;

    @BeforeClass
    public void setup() {
        System.setProperty("java.net.useSystemProxies", "false");
        System.setProperty("socksProxyHost", "");
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
        RestAssured.baseURI = APITests.utils.Config.get("books.api.url");
        RestAssured.useRelaxedHTTPSValidation();

        spec = new RequestSpecBuilder()
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Connection", "keep-alive")
                .build();
        token = obtenerToken();
    }

    protected String obtenerToken() {
        String email = generateRandomEmail();
        return given()

                .contentType("application/json")
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
