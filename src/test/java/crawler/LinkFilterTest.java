package crawler;

import config.ConfigurationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Link;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkFilterTest {
    private LinkFilter linkFilter;

    @BeforeEach
    void init() {
        linkFilter = new DefaultLinkFilter();
        ConfigurationUtils.configure();
    }

    @Test
    void shouldAcceptNormalLink() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/about")), new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksOnAnotherLanguage() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/fr")), new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksOnAnotherLanguageSegmentIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/fr/paris")), new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldAcceptLinksOnRightLanguage() {
        var langs = System.getProperty("site.langs").split(",");
        var filtered = linkFilter.filter(Set.of(new Link("example.com/" + langs[0] + "/")), new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksWithWrongFileExtension() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/index.java")), new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldAcceptLinksWithRightFileExtension() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/index.html")), new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksWithWrongFileExtensionSegmentIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/path/index.java")), new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldAcceptLinksWithRightFileExtensionSegmentIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/path/index.html")), new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldAcceptLinksWithRightFileExtensionQueryIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/index.html?value=true")),
                new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldAcceptLinksWithRightFileExtensionSubdomainIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("sub.example.com/path/index.html")), new Link("example.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksWithWrongFileExtensionQueryIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("example.com/index.java?value=true")),
                new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksWithWrongFileExtensionSubdomainIncluded() {
        var filtered = linkFilter.filter(Set.of(new Link("sub.example.com/path/index.java")), new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldNotAcceptLinksWithUserInfo() {
        var filtered = linkFilter.filter(Set.of(new Link("http://mailto:beate.nowak@zwick-edelstahl.de/impressm")),
                new Link("example.com"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldNotBeConfusedWithDotsInPath() {
        var filtered = linkFilter.filter(Set.of(new Link("http://www.jsoup.org/packages/jsoup-1.13.1.jar")),
                new Link("jsoup.org"));
        assertEquals(0, filtered.size());
    }

    @Test
    void shouldNotConsiderLinksWithFragmentDifferent() {
        var filtered = linkFilter.filter(
                Set.of(
                        new Link("https://github.com/features"),
                        new Link("https://github.com/features#hosting")
                ),
                new Link("github.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotConsiderLinksWithQueryDifferent() {
        var filtered = linkFilter.filter(
                Set.of(
                        new Link("https://github.com/features"),
                        new Link("https://github.com/features?value=1")
                ),
                new Link("github.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotConsiderLinksWithFragmentDifferent2() {
        var filtered = linkFilter.filter(
                Set.of(
                        new Link("https://www.am-computer.com/doku#workflows-prozesse"),
                        new Link("https://www.am-computer.com/doku#e-mail-archivierung")
                ),
                new Link("www.am-computer.com"));
        assertEquals(1, filtered.size());
    }

    @Test
    void shouldNotConsiderLinksWithDifferentProtocolsDifferent() {
        var filtered = linkFilter.filter(
                Set.of(
                        new Link("https://www.les-graveurs.de"),
                        new Link("http://www.les-graveurs.de")
                ),
                new Link("les-graveurs.de"));
        assertEquals(1, filtered.size());
    }
}
