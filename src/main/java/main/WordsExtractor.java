package main;

import config.ConfigurationUtils;
import database.DatabaseImpl;
import scraper.DefaultScraper;
import utils.CSVParser;
import utils.Html;
import utils.Link;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WordsExtractor {
    private static BlockingQueue<Link> linkQueue = new ArrayBlockingQueue<>(10000);
    private static BlockingQueue<Html> HtmlQueue = new ArrayBlockingQueue<>(10000);
    private static Map<String, Integer> sitesId = new HashMap<>();
    private static ThreadPoolExecutor EXECUTOR_SERVICE = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    public static volatile boolean work;

    public static void main(String[] args) throws Throwable {
        ConfigurationUtils.configure();
        var csvParser = new CSVParser();
        Map<String, Integer> domainsIds = csvParser.getDomainsIds();
        csvParser.parse("src/main/resources/websites_very_short.csv");
        List<Link> links = csvParser.getLinks();
        for (Link link : links) {
            linkQueue.add(link);
            DefaultScraper scraper = new DefaultScraper(linkQueue, HtmlQueue);
            var parser = new SiteParser.Builder(linkQueue,
                    HtmlQueue,
                    new DatabaseImpl(domainsIds),
                    link.getDomain(),
                    scraper).build();
            scraper.p(parser);
            var parserTask = EXECUTOR_SERVICE.submit(() -> {
                try {
                    parser.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            var scraperTask = EXECUTOR_SERVICE.submit(() -> {
                try {
                    scraper.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("main");
        }
    }

    void foo() {
    }
}
