package webscrape;

/**
 * Main class to run the web scraper.
 * Takes a URL as an argument and starts scraping from that URL.
 */
public class Main {
    private static final URIUtilities uriUtilities = new URIUtilities();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide a URL to scrape.");
            return;
        }

        final String scrapeURL = args[0];
        if (scrapeURL == null || scrapeURL.isEmpty() || !uriUtilities.validUrl(scrapeURL)) {
            System.out.println("Please provide a valid URL to scrape.");
            return;
        }

        WebScraper webScraper = new WebScraper();
        webScraper.scrape(scrapeURL);
    }
}

