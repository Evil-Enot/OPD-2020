package main;

import crawler.Crawler;
import crawler.LinkFilter;
import scraper.Scraper;
import utils.Link;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public class LinkCrawlingTask {
    private Scraper scraper;
    private Crawler crawler;
    private LinkFilter linkFilter;
    private Link link;
    private BlockingQueue<Link> linkQueue;

    public LinkCrawlingTask(Scraper s,
                    Crawler c,
                    LinkFilter lF,
                    Link l,
                    BlockingQueue<Link> q) {
        scraper = s;
        crawler = c;
        linkFilter = lF;
        link = l;
        linkQueue = q;
    }

    public Collection[] run() {
        try {
            var html = scraper.scrape(link);
            var links = crawler.crawl(html);
            var filteredLinks = linkFilter.filter(links, link);
            linkQueue.addAll(filteredLinks);
            return new Collection[] { links, filteredLinks };
        } catch (Exception e) {
            Main.consoleLog.error("SiteTask - Failed to run program: {}", e.toString());
            Main.debugLog.error("SiteTask - Failed to run program:", e);
            return new Collection[0];
        } finally {
            Main.completedTaskCount.incrementAndGet();
        }
    }
}
