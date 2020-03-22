package crawler;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Html;
import utils.Link;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DefaultCrawler implements Crawler {
    List<Link> dateList = new ArrayList<>();
    Link domain = new Link("");
    int count = 0;
    String lang = "";

    static ArrayList<String> langDate;
    static ArrayList<String> FileDate;

    static {
        try {
            langDate = new ArrayList<>(Files.readAllLines(Paths.get("src/main/resources/language.txt")));
            FileDate = new ArrayList<>(Files.readAllLines(Paths.get("src/main/resources/tagfiles.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Link> crawl(@NotNull Html html) {
        System.out.println("CRAWL");
        throw new NullPointerException();
//        var res = html.toDocument().select("a[href]")
//                .stream().map(it -> new Link(lastCharFixer(it.attr("abs:href")))).collect(Collectors.toList());
//        System.out.println(res);
//        return res;
    }

    private void reCrawl(@NotNull Html html) {
        Elements linksOnPage = html.toDocument().select("a[href]");
        for (Element page : linksOnPage) {
            Link url = new Link(lastCharFixer(page.attr("abs:href")));
            if (act(url, dateList)) {
//                System.out.println(url);
                count++;
                dateList.add(url);
            }
        }

    }

    public String lastCharFixer(@NotNull String inURL) {
        if (inURL.charAt(inURL.length() - 1) == "/".charAt(0))
            return inURL;
        return inURL + "/";
    }

    boolean act(@NotNull Link url, List<Link> dateList) {
        for (String name : FileDate)
            if (url.toString().substring(url.toString()
                    .length() - 7, url.toString().length() - 1)
                    .contains(name))
                return false;

        for (String name : langDate)
            if (url.toString().contains("/" + name + "/") && !url.toString().contains("/" + lang + "/"))
                return false;

        return !url.toString().contains("#") && !(url == domain) &&
                url.contains(domain) && !dateList.contains(url);
    }
}
