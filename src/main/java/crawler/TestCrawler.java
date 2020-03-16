package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HTML;
import util.Link;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCrawler {

    public static void main(String[] args) {

        String rootURL1 = urlFixer("https://www.zwick-edelstahl.de");//https://hakim.se/404 https://www.zwick-edelstahl.de/en https://www.albrecht-fachbau.de/
        try {
            Document document = Jsoup.connect(rootURL1).referrer("http://www.google.com").get();
            List<Link> zzz = new DefaultCrawler().crawl(new HTML(document, new Link(rootURL1)));
            for (Link each : zzz) System.out.println(each);
            System.out.println();
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException - нет инета");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String urlFixer(String inURL) {
        Matcher matcher3 = Pattern.compile("https://", Pattern.CASE_INSENSITIVE).matcher(inURL);
        Matcher matcher4 = Pattern.compile("http://", Pattern.CASE_INSENSITIVE).matcher(inURL);
        if (!matcher3.find() && !matcher4.find()) return "https://" + inURL;
        return inURL;
    }
}