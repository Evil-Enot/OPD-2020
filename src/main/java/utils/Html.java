package utils;

import org.jsoup.nodes.Document;

import java.util.Objects;

public class Html {
    private Document html;
    private Link url;

    public Html(Document html, Link url) {
        this.html = html;
        this.url = url;
    }

    public Html(String html, Link url) {
        this.html = new Document(html);
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
        Html html1 = (Html) o;
        return Objects.equals(html, html1.html) &&
                Objects.equals(url, html1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(html, url);
    }
}
