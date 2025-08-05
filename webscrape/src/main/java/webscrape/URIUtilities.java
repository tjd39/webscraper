package webscrape;

import java.net.URI;
import java.util.Set;

/**
 * Utility class for handling URIs in web scraping.
 * Provides methods to validate URLs, check authority matches, and manipulate URLs.
 */
public class URIUtilities {
    // Set of common file extensions that should not be scraped
    private final Set<String> commonFileExtensions = Set.of(
            ".7z", ".aac", ".asp", ".aspx", ".avi", ".bmp", ".css", ".csv", ".docx", ".exe", ".flac", ".flv", ".gif",
            ".gz", ".htm", ".html", ".ico", ".jpeg", ".jpg", ".js", ".json", ".jsonld", ".jsp", ".m4a", ".md", ".mkv",
            ".mov", ".mp3", ".mp4", ".ogg", ".pdf", ".php", ".png", ".pptx", ".rar", ".svg", ".tar", ".ts", ".txt",
            ".wav", ".webm", ".webp", ".wmv", ".xlsx", ".xml", ".yaml", ".yml", ".zip"
    );
    // Set of valid URL schemes for scraping
    private final Set<String> validSchemes = Set.of("http", "https");

    /**
     * Validates if the given URL is suitable for scraping.
     * This method checks if the URL has a valid scheme and path, and does not end with common file extensions.
     *
     * @param url The URL to validate.
     * @return true if the URL is valid for scraping, false otherwise.
     */
    public boolean validUrl(String url) {
        try {
            URI uri = new URI(url); // This will throw an exception if the URL is invalid
            return (uri.toString() == null || !uri.toString().isEmpty())
                    && validSchemes.contains(uri.getScheme())
                    && commonFileExtensions.stream().noneMatch(extension -> uri.toString().endsWith(extension));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the authority of the given URL matches the authority of the base URL.
     *
     * @param url The URL to check.
     * @param baseUrl The base URL to compare against.
     * @return true if the authorities match, false otherwise.
     */
    public boolean authoritiesMatch(String url, String baseUrl) {
        try {
            URI uriBase = new URI(baseUrl);
            URI uriTest = new URI(url);
            return uriBase.getAuthority().equals(uriTest.getAuthority());
        } catch (Exception e) {
            return false;

        }
    }

    /**
     * Retrieves the absolute URL, based on the base URL.
     * If the relative URL part is absolute it is not modified.
     *
     * @param url The URL to be resolved, can be relative or absolute.
     * @param baseUrl The base URL to resolve against, must be absolute.
     * @return The absolute URL as a string, minus query parameters and fragments.
     */
    public String getURLAbsolute(String url, String baseUrl) {
        try {
            String output = "";
            URI relativeUri = new URI(url);
            if (relativeUri.isAbsolute()) {
                output = relativeUri.toString(); // If it's already absolute, return it
            } else {
                output = new URI(baseUrl).resolve(relativeUri).toString();
            }

            if (output.endsWith("/")) {
                output = output.substring(0, output.length() - 1); // Remove trailing slash if present
            }
            return output;
        } catch (Exception e) {
            return url; // If the URL is invalid, return it as is
        }
    }

    /**
     * Returns the URL with scheme, authority, and path (parameters and fragments are discarded).
     *
     * @param url The URL to process.
     * @return The URL without query parameters and fragments.
     */
    public String getURLWithoutQueryAndFragment(String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
        } catch (Exception e) {
            return url; // If the URL is invalid, return it as is
        }
    }
}
