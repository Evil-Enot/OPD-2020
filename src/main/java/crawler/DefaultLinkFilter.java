package crawler;

<<<<<<< HEAD
import utils.Link;

import org.jetbrains.annotations.NotNull;
import utils.Link;

=======
import org.jetbrains.annotations.NotNull;
import util.Link;

>>>>>>> 11cc3b5d5a67acd9b7688b79ca4d30c7290780f9
import java.util.ArrayList;
import java.util.List;

public class DefaultLinkFilter implements LinkFilter {

    @Override
    public List<Link> filter(@NotNull List<Link> links, String domain) {
        List<Link> out = new ArrayList<>();
        for (Link link : links) {
            if (!out.contains(link))
                if (link.toString().contains(domain)) {
                    if (!link.toString().contains("#"))
                        out.add(link);
                }
        }
        return out;
    }
}
