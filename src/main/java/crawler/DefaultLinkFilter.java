package crawler;

import org.jetbrains.annotations.NotNull;
import utils.Link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DefaultLinkFilter implements LinkFilter {

    Collection<Link> visitedLinks = new HashSet<>();

    @Override
    public List<Link> filter(@NotNull List<Link> links, String domain) {
        ArrayList<Link> res = new ArrayList<>();
        for (Link link : links) {
            if (!visitedLinks.contains(link) && link.getDomain().equals(domain)) {
                res.add(link);
                visitedLinks.add(link);
            }
        }
        return res;
    }

//    private Collection<Link> langFilter(Collection<Link> links) {
//
//    }
}
