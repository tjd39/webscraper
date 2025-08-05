import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;

import webscrape.WebScraper;

/**
 * Simple unit test for the web scraper
 */
public class TestWebScraper {
    private ClassLoader classLoader;

    @Spy
    private WebScraper webScraper;

    @Captor
    private ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);

    @Before
    public void setUp() {
        webScraper = new WebScraper();
        webScraper = spy(webScraper);
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    @Test
    public void testScrape() throws IOException {
        // Given
        for (TestURL url : TestURL.values()) {
            doReturn(testHtml(url)).when(webScraper).getHtml(eq(url.url()));
        }

        // When
        webScraper.scrape("https://example.com");

        // Then
        assertThat(webScraper.getVisitedURLs().size(), is(4));
        assertThat(webScraper.getLinksFound().size(), is(13));
    }

    private String testHtml(final TestURL url) throws IOException {
        final File testHTMLFile = new File(classLoader.getResource(url.filePath()).getPath());
        return new String(java.nio.file.Files.readAllBytes(testHTMLFile.toPath()));
    }
}
