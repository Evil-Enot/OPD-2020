import com.github.tomakehurst.wiremock.WireMockServer;
import config.ConfigurationUtils;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.HtmlUtils;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsoupTest {
    @BeforeAll
    static void configure() {
        ConfigurationUtils.configure();
    }

    @Test
    public void testMyDynamicSite() throws IOException {
        String projectPath = System.getProperty("project.path");

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withBody(HtmlUtils.fileToHtml(projectPath + "/src/test/resources/DriverTestRes/index.html"))));
        stubFor(get(urlEqualTo("/first_button"))
                .willReturn(aResponse().withBody("<div id=\"worked\">Worked</div>")));
        try {
            assertFalse(Jsoup.connect("http://localhost:8080").get().text().contains("Worked"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
