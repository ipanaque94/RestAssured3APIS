package APITests.baseUrl;

import org.testng.annotations.BeforeClass;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RedirectConfig;
import io.restassured.specification.RequestSpecification;

public class BaseDataAcceess {

    protected RequestSpecification spec;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config = RestAssured.config()
                .redirect(RedirectConfig.redirectConfig()
                        .followRedirects(true)
                        .maxRedirects(5));

        spec = new RequestSpecBuilder()
                .addHeader("Content-Type", "text/xml; charset=UTF-8")
                .addHeader("SOAPAction", "")
                .build();
    }
}