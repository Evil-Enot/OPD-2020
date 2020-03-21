import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import config.ConfigurationUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class WireMockTest {

    @BeforeAll
    static void configure() {
        ConfigurationUtils.configure();
    }

    @Test
    public void responseTest() {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.stubFor(
                get(
                        urlEqualTo("/first_button"))
                .willReturn(
                        aResponse().withBody("<div id=\"worked\">Worked</div>")));

        String projectPath = System.getProperty("project.path");

        WebDriver driver = new ChromeDriver();
        driver.get("file://" + projectPath + "/src/test/resources/DriverTestRes/index.html");
        var t = driver.findElement(By.id("content-button-1"));
        t.click();
        assertDoesNotThrow(() -> new WebDriverWait(driver, 1).until(it -> it.findElement(By.id("worked"))));
    }

    @Test
    public void r() {

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.stubFor(get(urlEqualTo("/first_button"))
                .willReturn(aResponse().withBody("<div id=\"worked\">Worked</div>")));
        stubFor(get(urlEqualTo("/pic"))
                .willReturn(aResponse().withBodyFile("/pic.png")));
        stubFor(get(urlEqualTo("/index.html"))
                .willReturn(aResponse().withBodyFile("index.html")));


        while (true) {}
    }
}
