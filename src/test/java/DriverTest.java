import com.github.tomakehurst.wiremock.WireMockServer;
import config.ConfigurationUtils;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DriverTest {

    public String fileHtml(String relativePath) {
        return "file://" + projectPath + relativePath;
    }

    WebDriver driver;
    WebDriver.Options options;
    String projectPath = System.getProperty("project.path");
    WireMockServer wireMockServer;

    @BeforeAll
    static void configure() {
        ConfigurationUtils.configure();
    }

    void initMockServer() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @BeforeEach
    void initDriver() {
        var options = new ChromeOptions();
        options.addArguments("--headless");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void closeBrowser() {
//        driver.quit();
    }

    @Test
    public void clickTest() {
        driver.get("file://" + projectPath + "src/test/resources/dynamicSite/main.html");
        var el = driver.findElement(By.id("first-button"));
        el.click();
        assertEquals("file://" + projectPath
                + "src/test/resources/dynamicSite/files/index1.html", driver.getCurrentUrl());
    }

    @Test
    public void setContentTest() {
        driver.get("file://" + projectPath + "src/test/resources/dynamicSite/main.html");
        var el = driver.findElement(By.id("first-button"));
        el.sendKeys("ddddddd");
    }

    @Test
    public void navigateTest() {
        driver.navigate().to("file://" + projectPath + "src/test/resources/dynamicSite/main.html");
    }

    @Test
    public void explicitWaitTest() {
        driver.get("file://" + projectPath + "src/test/resources/DriverTestRes/1.html");
        WebElement el = new WebDriverWait(driver, 3)
                .until(it -> it.findElement(By.tagName("p")));
        assertEquals(el.getText(), "Hello from JavaScript!");
    }

    @Test
    public void showMeMyPage() {
        driver.get("file://" + projectPath + "src/test/resources/DriverTestRes/index.html");
    }

    @Test
    public void clickOnButtons() {
        driver.get("file://" + projectPath + "src/test/resources/scraper_res/button_rewrite/button_rewrite.html");
        System.out.println(driver.getPageSource());
        var el = driver.findElement(By.id("rewrite-button"));
        el.click();
    }

    @Test
    public void getJSCreatedContent() {
        driver.get(fileHtml("src/test/resources/scraper_res/js_created/js_created.html"));
    }

    @Test
    public void ignorePics() {
        initMockServer();
        stubFor(get(urlEqualTo("/pic"))
                .willReturn(aResponse().withBodyFile("/pic.png").withFixedDelay(20000)));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(fileHtml("src/test/resources/scraper_res/ignore_pics/ignore_pics.html"));
    }

    // важна обработка редиректов
    // убирать функции с аналитикой
    @Test
    public void sliderTest() {
        driver.get(fileHtml("src/test/resources/werner/Werner " +
                "- Industrievertretung. Distributor für Elektrotechnik. - Saarbrücken.html"));
        assertTrue(Jsoup.parse(driver.getPageSource()).text().contains("Produkte und Systeme für komplexe Gebäudetechnik."));
    }

    @Test
    public void popUpTest() {
        driver.get("https://www.wolfsperger-landmaschinen.de/");
//        assertFalse(Jsoup.parse(driver.getPageSource()).text().contains("Diese Website verwendet Cookies"));
        assertTrue(Jsoup.parse(driver.getPageSource()).text().contains("Seiteninhalte wieder anzeigen"));
    }

    @Test
    public void hideScript() {
        driver.get(fileHtml("src/test/resources/wolfsperger/Herzlich Willkommen - Wolfsperger Landmaschinen.html"));
        assertTrue(Jsoup.parse(driver.getPageSource()).text().contains("Seiteninhalte wieder anzeigen"));
    }

    // бывают редиректы, которые отправляют на другие сайты
    // фильтровать url параметры. Они могут вести к новым страницам, а могут увеличивать шрифт (https://www.rosenapotheke.cc/seitenuebersicht/?CSS=1)
    // хмм, а как же всякая фигня для гугл поиск?

//    datenschutzerklaerung -- политика данных
//    как переводчик хрома получает все слова на странице?
    @Test
    public void grass() {
        driver.get("https://www.wolfsperger-landmaschinen.de/kontakt/");
        assertTrue(Jsoup.parse(driver.getPageSource()).text().contains("Bitte den Code eingeben"));
    }

//    http://oriental-shop.com лежит, причём возвращает 200

//    gallzick разные языки

    @Test
    public void vypadaushiySpisok() {
        driver.get("http://gallzick.com/disclaimer/");
        assertTrue(Jsoup.parse(driver.getPageSource()).text().contains("Wandbilder u. Skulpturen"));
    }

//    поаккуратнее с инпутами не везде они нужны
//    У gallzick есть ordershop
//    какие там выпадающие списки? Валюта, порядок сортировки, кол-во товаров на странице, лист или плитка.
//    button в корзину, которая onclick=setLocation()
//    кнопка поиска

//    http://www.zwick-edelstahl.de/en/ меняет вид в маленьком окне
//    karriere

//    у https://nagel-gruppe.de/ url зависят от языка

//    https://ad1950.de/ выдаёт страницу coming soon

//    https://albrecht-dill.de/ кнопочка, которая откидывает тебя наверх
//    Как говорится, сингл пэйдж апликейшин

//    зашита от спама капча https://www.alkoma.de/
//    сингл пейдж апликатион + блог

//    https://www.albrecht-gerber.de/index-ag.html
//    здесь всё с помощью url параметров
//    есть архив
//    Такое по ссылочкам не распарсишь

//    всё через ?id http://velte-steinmetz.de
//    js код на страницах валяется
//    страничка перерисовывается http://velte-steinmetz.de/naturstein.htm
//    http://velte-steinmetz.de/broschuere.htm 404

//    партнёров нужно отцеживать


}
