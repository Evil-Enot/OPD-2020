package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HTML;
import util.Link;

import java.io.IOException;
import java.util.List;

public class TestCrawler {
    public static String rootURL1 = "https://jsoup.org/";
    public static String rootURL2 = "https://github.com/Kvarki-rM/KotlinAsFirst2018/";

    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect(rootURL1).referrer("http://www.google.com").get();
            List<Link> zzz = new DefaultCrawler().crawl(new HTML(document, new Link(rootURL1)));

            for (Link each : zzz) System.out.println(each);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}