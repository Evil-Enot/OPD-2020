package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HTML;
import util.Link;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DefaultCrawler implements Crawler {
    List<Link> dateList = new ArrayList<>();
    Link domain = new Link("");
    int count = 0;

    @Override
    public List<Link> crawl(@NotNull HTML html) {
        domain = html.getUrl();
        List<Link> dateList = new ArrayList<>();
        reCrawl(html);
        return dateList;
    }

    private void reCrawl(@NotNull HTML html) {
        Elements linksOnPage = html.toDocument().select("a[href]");
        for (Element page : linksOnPage) {
            Link url = new Link(page.attr("abs:href"));
            if (act(url, dateList)) {
                System.out.println(url);
                dateList.add(url);
                try {
                    Document doc = Jsoup.connect(url.toString())
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .timeout(1000 * 5)
                            .get();
                    reCrawl(new HTML(doc, url));

                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean act(@NotNull Link url, List<Link> dateList) {
        return !url.toString().contains("#") && !(url == domain) &&
                url.contains(domain) && !dateList.contains(url);
    }
}





