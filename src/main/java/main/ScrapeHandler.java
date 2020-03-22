package main;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.Html;
import utils.Link;

import java.util.concurrent.BlockingQueue;

public class ScrapeHandler {
    private BlockingQueue<Link> linkQueue;
    private BlockingQueue<Html> HtmlQueue;
    volatile boolean isWorking = true;

    WebDriver driver;


    public ScrapeHandler(BlockingQueue<Link> linkQueue, BlockingQueue<Html> htmlQueue) {

        this.linkQueue = linkQueue;
        HtmlQueue = htmlQueue;
    }

    public void start() throws InterruptedException {
        do {
            isWorking = false;
            var t = scrape(linkQueue.take());
            isWorking = true;
            HtmlQueue.add(t);
        } while (!HtmlQueue.isEmpty() || !linkQueue.isEmpty());
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
}
