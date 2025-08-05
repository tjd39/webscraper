public enum TestURL {
    MAIN("https://example.com", "main.html"),
    SOCIAL("https://example.com/socials", "socials.html"),
    JOBS("https://example.com/jobs", "jobs.html"),
    CONTACT("https://example.com/contact", "contact.html");

    private final String url;
    private final String filePath;

    TestURL(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    public String url() {
        return url;
    }

    public String filePath() {
        return filePath;
    }
}
