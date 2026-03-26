package APITests.dataaccess;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SoapTest {

    @Severity(SeverityLevel.NORMAL)
    @Description("POST SOAP NumberToWords con ubiNum=500 debe retornar 'five hundred' en la respuesta XML")
    @Story("Conversión de número a palabras")
    @Owner("Enoc Ipanaque")
    @Test(groups = { "SOAP", "Smoke" })
    public void testNumeroApalabras500() {
        String endpoint = "http://www.dataaccess.com/webservicesserver/numberConversion.wso";

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

        try {
            Response response = RestAssured.given()
                    .contentType("text/xml; charset=UTF-8")
                    .header("SOAPAction", "")
                    .body(soapBody)
                    .relaxedHTTPSValidation() // ← Ignorar SSL
                    .when()
                    .post(endpoint);

            Assert.assertEquals(response.getStatusCode(), 200,
                    "Status code incorrecto. Response: " + response.asString());

            String numberInWords = response.xmlPath()
                    .getString("//*[local-name()='NumberToWordsResult']")
                    .trim();

            Assert.assertEquals(numberInWords, "five hundred",
                    "Número convertido incorrecto");

        } catch (Exception e) {
            Assert.fail("Test SOAP falló debido a: " + e.getMessage());
        }
    }
}