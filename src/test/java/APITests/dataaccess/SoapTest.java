package APITests.dataaccess;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseDataAcceess;

import static io.restassured.RestAssured.given;

public class SoapTest extends BaseDataAcceess {

    @Severity(SeverityLevel.NORMAL)
    @Description("POST SOAP NumberToWords con ubiNum=500 debe retornar 'five hundred'")
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

        Response response = given()
                .spec(spec)
                .body(soapBody)
                .when()
                .post("https://www.dataaccess.com/webservicesserver/numberconversion.wso");

        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.asString());

        Assert.assertEquals(response.statusCode(), 200,
                "Status incorrecto. Response: " + response.asString());

        String result = response.xmlPath()
                .getString("//*[local-name()='NumberToWordsResult']")
                .trim();

        Assert.assertEquals(result, "five hundred");
    }
}