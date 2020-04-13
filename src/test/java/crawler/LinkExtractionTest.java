package crawler;

import config.ConfigurationUtils;
import main.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scraper.DefaultScraper;
import utils.Html;
import utils.Link;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LinkExtractionTest {
    DefaultLinkFilter filter = new DefaultLinkFilter();
    DefaultCrawler crawler = new DefaultCrawler();
    DefaultScraper scraper = new DefaultScraper();

    @BeforeEach
    void configure() {
        ConfigurationUtils.configure();
    }

    @Test
    void scrapePage() throws IOException {
        Link domain = new Link("ruwi-dachdecker.de");
        String link = "http://ruwi-dachdecker.de";
        Html html = scraper.scrape(new Link(link));
        var links = crawler.crawl(html);
        var filteredLinks = filter.filter(links, domain);
        for (Link l : filteredLinks) {
            System.out.println(l);
        }
        System.out.println("______------------________________________----------------_________________");
        for (Link l : links) {
            if (!filteredLinks.contains(l)) {
                System.out.println(l);
            }
        }
    }

    @Test
    void scrapeSite() throws IOException, ExecutionException, InterruptedException {
        Link domain = new Link("lehatrans.at");
        var res = Main.runWithoutWordsExtracting(domain);
        var all = res[0];
        var filtered = res[1];
        for (Object l : filtered) {
            System.out.println(l);
        }
        System.out.println("______------------________________________----------------_________________");
        for (Object l : all) {
            if (!filtered.contains(l)) {
                System.out.println(l);
            }
        }
    }
}
