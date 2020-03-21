package extractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.Html;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class DefaultExtractor implements Extractor {

    public Collection<String> extract(Html html) {
        Document doc = Jsoup.parse(html.toString());
        String allInfo = doc.text();
        String[] stringsArray;
        stringsArray = allInfo.split("\\s");
        return new HashSet<>(Arrays.asList(stringsArray));
    }
}
