package util;

import org.jsoup.nodes.Document;

import java.util.Objects;

public class HTML {
    private Document html;
    private Link url;

    public HTML(Document html, Link url) {
        this.html = html;
        this.url = url;
    }

    public Link getUrl() {
        return url;
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
