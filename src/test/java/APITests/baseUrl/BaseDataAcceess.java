package APITests.baseUrl;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;

public class BaseDataAcceess {
    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = APITests.utils.Config.get("soap.api.url");
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config = RestAssured.config()
                .redirect(RedirectConfig.redirectConfig()
                        .followRedirects(true)
                        .maxRedirects(5));

    }

}
