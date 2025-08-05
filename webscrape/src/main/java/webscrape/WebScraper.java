package webscrape;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A simple web scraper implementation that:
 * - Visits a URL
 * - Prints all links found at that URL
 * - Visits each URL it finds on the same domain.
 */
public class WebScraper {
    private final Set<String> visitedURLs = new HashSet<>();
    private final Set<String> linksFound = new HashSet<>();
    private final URIUtilities uriUtilities = new URIUtilities();
    String baseURL;

    // to make testing easier
    public String getHtml(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.outerHtml();
    }

    public void scrape(String scrapeURL) {
        String url = uriUtilities.getURLWithoutQueryAndFragment(scrapeURL);
        if (visitedURLs.isEmpty() && (baseURL == null || baseURL.isEmpty())) {
            baseURL = url; // set base URL on first scrape
        }

        if (visitedURLs.contains(url)) {
            return; // already visited this URL
        }

        visitedURLs.add(url);
        System.out.println("# " + url);

        // get document and links
        Document document;
        try {
            document = Jsoup.parse(getHtml(url));
        } catch (IOException e) {
            return; // ignore IO exceptions, e.g. 404 errors
        }
        Elements links = document.getElementsByTag("a"); //Todo: there may be links that are not <a> tags

        // process links
        Set<String> linksToScrape = new HashSet<>();
        for (Element link : links) {
            String linkURL = link.attr("href");
            linksFound.add(linkURL);
//            System.out.println("  - " + linkURL);

            linkURL = uriUtilities.getURLAbsolute(linkURL, baseURL);
            linkURL = uriUtilities.getURLWithoutQueryAndFragment(linkURL);
            if (uriUtilities.validUrl(linkURL)
                    && uriUtilities.authoritiesMatch(linkURL, baseURL)
                    && !visitedURLs.contains(linkURL)) {
                // valid, fully qualified link within domain
                linksToScrape.add(linkURL);
            }
        }

        // scrape links
        linksToScrape.forEach(this::scrape);
    }

    public Set<String> getVisitedURLs() {
        return visitedURLs;
    }

    public Set<String> getLinksFound() {
        return linksFound;
    }

    public void print() {
        System.out.println(String.format("Visited %d URLs.", getVisitedURLs().size()));
        System.out.println(String.format("Found %d links.", getLinksFound().size()));
        System.out.println("Visited URLs:");
        visitedURLs.forEach(System.out::println);
        System.out.println("Links found:");
        linksFound.forEach(System.out::println);
    }
}
