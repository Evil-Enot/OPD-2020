package main;

import crawler.Crawler;
import crawler.DefaultCrawler;
import crawler.DefaultLinkFilter;
import crawler.LinkFilter;
import database.Database;
import extractor.DefaultExtractor;
import extractor.DefaultWordFilter;
import extractor.Extractor;
import extractor.WordFilter;
import scraper.DefaultScraper;
import utils.Html;
import utils.Link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SiteParser {

    public static class Builder {
        BlockingQueue<Link> linkQueue;
        BlockingQueue<Html> HtmlQueue;
        Database db;
        String domain;

        Crawler crawler = new DefaultCrawler();
        Extractor extractor = new DefaultExtractor();
        WordFilter wordFilter = new DefaultWordFilter();
        LinkFilter linkFilter = new DefaultLinkFilter();
        DefaultScraper c;

        public Builder(BlockingQueue<Link> linkQueue, BlockingQueue<Html> HtmlQueue, Database db,
                       String domain, DefaultScraper c) {
            this.linkQueue = linkQueue;
            this.HtmlQueue = HtmlQueue;
            this.db = db;
            this.domain = domain;
            this.c = c;
        }

        public Builder crawler(Crawler value) {
            crawler = value;
            return this;
        }

        public Builder extractor(Extractor value) {
            extractor = value;
            return this;
        }

        public Builder wordFilter(WordFilter value) {
            wordFilter = value;
            return this;
        }

        public Builder linkFilter(LinkFilter value) {
            linkFilter = value;
            return this;
        }

        public SiteParser build() {
            return new SiteParser(this);
        }
    }

    private BlockingQueue<Link> linkQueue;
    private BlockingQueue<Html> HtmlQueue;
    private Database db;
    private String domain;
    private Crawler crawler;
    private Extractor extractor;
    private WordFilter wordFilter;
    private LinkFilter linkFilter;
    DefaultScraper c;

    public SiteParser(Builder builder) {
        linkQueue = builder.linkQueue;
        HtmlQueue = builder.HtmlQueue;
        db = builder.db;
        domain = builder.domain;
        crawler = builder.crawler;
        extractor = builder.extractor;
        wordFilter = builder.wordFilter;
        linkFilter = builder.linkFilter;
        c = builder.c;
    }

    private final ThreadPoolExecutor EXECUTOR_SERVICE = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    private final Collection<String> tempCollection = new ArrayList<>();

//    public void start() throws InterruptedException {
//        do {
//            var html = HtmlQueue.take();
//            crawler.crawl(html);
//            CompletableFuture<List<Link>> crawlerFuture = CompletableFuture.supplyAsync(
//                    () -> crawler.crawl(html),
//                    EXECUTOR_SERVICE);
//            crawlerFuture.thenAccept(result -> linkQueue.addAll(linkFilter.filter(result, domain)));
//            CompletableFuture<Collection<String>> extractorFuture = CompletableFuture.supplyAsync(
//                    () -> extractor.extract(html),
//                    EXECUTOR_SERVICE);
//            extractorFuture.thenAccept(result -> tempCollection.addAll(wordFilter.filter(result)));
//        } while (true);
////        System.out.println("Site parser is ready");
////        EXECUTOR_SERVICE.shutdown();
//    }

    public volatile boolean isWorking = true;

    public void start() throws InterruptedException {
        do {
            isWorking = false;
            var html = did();
            isWorking = true;
            if (html == null) break;
            var result = crawler.crawl(html);
            linkQueue.addAll(linkFilter.filter(result, domain));
        } while (true);
    }

    public synchronized Html did() throws InterruptedException {
        if (HtmlQueue.isEmpty() && !c.isWorking) {
            return null;
        } else {
            return HtmlQueue.take();
        }
    }
}