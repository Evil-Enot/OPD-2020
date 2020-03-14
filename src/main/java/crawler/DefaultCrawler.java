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

    Link domain = new Link("");

    @Override
    public List<Link> crawl(@NotNull HTML html) {
        domain = html.getUrl();
        List<Link> dateList = new ArrayList<>();
        reCrawl(html, dateList);
        return dateList;
    }

    private void reCrawl(@NotNull HTML html, List<Link> dateList) {
        Elements linksOnPage = html.toDocument().select("a[href]");
        for (Element page : linksOnPage) {
            Link url = new Link(page.attr("abs:href"));
            if (!url.toString().contains("#"))
                if (!(url == domain) && url.contains(domain) && !dateList.contains(url)) {
                    dateList.add(url);
                    try {
                        System.out.println(url);
                        Document doc = Jsoup.connect(url.toString())
                                .ignoreContentType(true)
                                .ignoreHttpErrors(true)
                                .timeout(1000 * 5)
                                .get();
                        reCrawl(new HTML(doc, url), dateList);

                    } catch (NullPointerException | IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}





