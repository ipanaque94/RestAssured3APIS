package APITests.baseUrl;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseRickMorty {
    protected static RequestSpecification spec;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = APITests.utils.Config.get("rick.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        spec = new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .build();
    }

}
