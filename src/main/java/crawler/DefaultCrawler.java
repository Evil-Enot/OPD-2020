package crawler;

import org.jsoup.Jsoup;
import util.HTML;
import util.Link;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DefaultCrawler implements Crawler {

    @Override
    public List<Link> crawl(@NotNull HTML html) {
        List<Link> list = new ArrayList<>();
        /// Document doc = Jsoup.parseBodyFragment(html.toString());
        Elements linksOnPage = html.toDocument().select("a[href]");

        for (Element page : linksOnPage) {
            Link url = new Link(page.attr("abs:href"));
            if (!(url == html.getUrl()))
                list.add(url);
        }
        return list;
    }
}


