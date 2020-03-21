import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import config.ConfigurationUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.HtmlUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CodeInjectionTest {

    @BeforeAll
    static void configure() {
        ConfigurationUtils.configure();
    }

    @Test
    public void injectTest() throws IOException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.stubFor(
                get(
                        urlEqualTo("/first_button"))
                        .willReturn(
                                aResponse().withBody("<div id=\"worked\">Worked</div>")));
        String projectPath = System.getProperty("project.path");
        Document j = Jsoup.parse(HtmlUtils.fileToHtml(projectPath + "/src/test/resources/DriverTestRes/index.html"));
        j.body().append("<script type=\"text/javascript\" src=\"2.js\"></script>");
        Files.write(Paths.get("src/test/resources/test.html"), j.toString().getBytes());
        WebDriver driver = new ChromeDriver();
        driver.get("file://" + projectPath + "/src/test/resources/test.html");
    }
}
