package APITests.dataaccess;

import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import APITests.baseUrl.BaseDataAcceess;

import static io.restassured.RestAssured.given;

@Epic("DataAccess SOAP API")
@Feature("NumberConversion")
@Link("https://www.dataaccess.com/webservicesserver/numberconversion.wso")
public class SoapTest extends BaseDataAcceess {

    private static final Logger log = LoggerFactory.getLogger(SoapTest.class);

    @Severity(SeverityLevel.NORMAL)
    @Description("POST SOAP NumberToWords con ubiNum=500 debe retornar 'five hundred'")
    @Story("Conversión de número a palabras")
    @Owner("Enoc Ipanaque")
    @Tags({ @Tag("SOAP"), @Tag("Test Positivo") })
    @Test(groups = { "SOAP", "Smoke" })
    public void testNumeroApalabras500() {
        Allure.getLifecycle().updateTestCase(
                tc -> tc.setName("Valida que el servicio SOAP NumberToWords convierte 500 en 'five hundred'"));
        log.info("Iniciando test: testNumeroApalabras500");

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

        Response response = given().spec(spec).body(soapBody)
                .when().post("https://www.dataaccess.com/webservicesserver/numberconversion.wso");

        Assert.assertEquals(response.statusCode(), 200,
                "Status incorrecto. Response: " + response.asString());

        String result = response.xmlPath()
                .getString("//*[local-name()='NumberToWordsResult']").trim();
        Assert.assertEquals(result, "five hundred");
        log.info("Test completado exitosamente — resultado: {}", result);
    }
}