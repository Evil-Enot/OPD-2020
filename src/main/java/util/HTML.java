package util;

import org.jsoup.nodes.Document;

import java.util.Objects;

public class HTML {
    private Document html;
    private Link url;
    private String language;

    public HTML(Document html, Link url) {
        this.html = html;
        this.url = url;
        this.language = this.html.selectFirst("html").attr("lang");
    }

    public Link getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return html.html();
    }

    public Document toDocument() {
        return html;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HTML html1 = (HTML) o;
        return Objects.equals(html, html1.html) &&
                Objects.equals(url, html1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(html, url);
    }
}
