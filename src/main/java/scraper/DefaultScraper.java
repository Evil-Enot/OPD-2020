package scraper;

import main.SiteParser;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.Html;
import utils.Link;

import java.util.concurrent.BlockingQueue;

public class DefaultScraper implements Scraper {

    private BlockingQueue<Link> linkQueue;
    private BlockingQueue<Html> HtmlQueue;
    WebDriver driver;
    public volatile boolean isWorking = true;
    SiteParser p;

    public DefaultScraper(BlockingQueue<Link> linkQueue, BlockingQueue<Html> HtmlQueue) {
        this.linkQueue = linkQueue;
        this.HtmlQueue = HtmlQueue;
    }

    public void p(SiteParser p) {
        this.p = p;
    }
    public void start() throws InterruptedException {
        do {
            isWorking = false;
            var t = doWhatIToldYou();
            isWorking = true;
            if (t == null) break;
            HtmlQueue.add(t);
        } while (true);
        System.out.println("Scraper is ready");
    }

    private Html scrape(Link link) {
        try {
            var options = new ChromeOptions();
            options.addArguments("--headless");
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            driver = new ChromeDriver(options);
            driver.get(link.toString());
            return new Html(driver.getPageSource(), new Link(driver.getCurrentUrl()));
        } finally {
            driver.quit();
        }
    }

    private synchronized Html doWhatIToldYou() throws InterruptedException {
        if (linkQueue.size() == 0 && !p.isWorking) {
            return null;
        } else {
            return scrape(linkQueue.take());
        }
    }
}
