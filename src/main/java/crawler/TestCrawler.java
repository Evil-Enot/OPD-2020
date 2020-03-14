package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HTML;
import util.Link;

import java.io.IOException;
import java.util.List;

public class TestCrawler {
    public static String rootURL = "https://jsoup.org/";
    public static String rootURL2 = "https://jsoup.org/download";

    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect(rootURL).get();
            List<Link> zzz = new DefaultCrawler().crawl(new HTML(document, new Link(rootURL)));

            document = Jsoup.connect(rootURL2).get();
            zzz.addAll(new DefaultCrawler().crawl(new HTML(document, new Link(rootURL))));

            for (Link each : zzz) {
                System.out.println(each);
            }
            System.out.println("--------------------------------sss--------------------------------");
            List<Link> temp = new DefaultLinkFilter().filter(zzz,rootURL);

            for (Link each : temp) {
                System.out.println(each);
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}