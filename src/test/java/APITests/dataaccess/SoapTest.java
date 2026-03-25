package APITests.dataaccess;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class SoapTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://www.dataaccess.com";
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .build();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("POST SOAP NumberToWords con ubiNum=500 debe retornar 'five hundred' en la respuesta XML")
    @Story("Conversión de número a palabras")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "SOAP", "Smoke" })
    public void testNumeroApalabras500() {
        String soapBody = """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                    <soap:Body>
                        <NumberToWords xmlns="http://www.dataaccess.com/webservicesserver/">
                            <ubiNum>500</ubiNum>
                        </NumberToWords>
                    </soap:Body>
                </soap:Envelope>
                """;

        given()

                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "")
                .body(soapBody)
                .when()
                .post("/webservicesserver/numberconversion.wso")
                .then()
                .statusCode(200)
                .body(containsString("five hundred"));
    }
}