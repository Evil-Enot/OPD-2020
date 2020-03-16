package crawler;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.HTML;
import util.Link;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLProtocolException;

public class DefaultCrawler implements Crawler {
    List<Link> dateList = new ArrayList<>();
    Link domain = new Link("");
    int count = 0;
    String lang = "";

    static ArrayList<String> langDate;
    static ArrayList<String> FileDate;

    static {
        try {
            langDate = new ArrayList<>(Files.readAllLines(Paths.get("src\\main\\resources\\language.txt")));
            FileDate = new ArrayList<>(Files.readAllLines(Paths.get("src\\main\\resources\\tagfiles.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Link> crawl(@NotNull HTML html) {
        lang = html.getLanguage();
        domain = html.getUrl();
        reCrawl(html);
        return dateList;
    }

    private void reCrawl(@NotNull HTML html) {
        Elements linksOnPage = html.toDocument().select("a[href]");
        for (Element page : linksOnPage) {
            Link url = new Link(lastCharFixer(page.attr("abs:href")));
            if (act(url, dateList)) {
                System.out.println(url);
                count++;
                dateList.add(url);
                try {
                    Document doc = Jsoup.connect(url.toString())
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .maxBodySize(0)
                            .timeout(5 * 1000)
                            .get();
                    reCrawl(new HTML(doc, url));
                } catch (SocketTimeoutException e) {
                    System.out.println("Превышено время подключения(.timeout(5 * 1000)) " + url);
                } catch (SSLProtocolException e) {
                    System.out.println("SLProtocolException");
                } catch (HttpStatusException e) {
                    System.out.println("Ошибка 404??? " + url);
                } catch (UnknownHostException e) {
                    System.out.println("UnknownHostException - нет инета");
                } catch (NullPointerException | IOException e) {
                    System.out.println(url);
                    e.printStackTrace();
                }
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





