package org.example.WebCrawler;

//Design a Web Crawler that starts from a given URL, fetches web pages, extracts links, and
//continues crawling while avoiding duplicates.
//The crawler must respect limits like max pages and concurrency, and be easily extensible.

import java.util.*;
import java.util.concurrent.*;

class WebCrawler {
    private final URLFrontier frontier;
    private final VisitedURLStore visited;
    private final PageFetcher fetcher;
    private final LinkExtractor extractor;
    private final ExecutorService pool;
    private final int maxPages;

    WebCrawler(String seedUrl, int maxpages, int threads) {
        this.frontier = new URLFrontier();
        this.visited = VisitedURLStore.getInstance();
        this.fetcher = new PageFetcher();
        this.extractor = new LinkExtractor();
        this.pool = Executors.newFixedThreadPool(threads);
        this.maxPages = maxpages;
        frontier.add(seedUrl);
    }

    void start() {
        for (int i = 0; i < maxPages; i++) {
            pool.execute(new CrawlerWorker());
        }

        pool.shutdown();
    }

    class CrawlerWorker implements Runnable {
        public void run() {
            String url = frontier.get();
            if (url == null) return;

            if (!visited.markVisited(url)) return;

            String html = fetcher.fetch(url);
            List<String> links = extractor.extract(html);

            for (String link : links) {
                frontier.add(link);
            }
        }
    }
}

class URLFrontier {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    void add(String url) {
        queue.offer(url);
    }

    String get() {
        return queue.poll();
    }
}

class VisitedURLStore {
    private static final VisitedURLStore INSTANCE = new VisitedURLStore();
    private final Set<String> visited = ConcurrentHashMap.newKeySet();

    private VisitedURLStore() {
    }

    static VisitedURLStore getInstance() {
        return INSTANCE;
    }

    boolean markVisited(String url) {
        return visited.add(url);
    }
}

class PageFetcher {
    String fetch(String url) {
        return "<>html><a href = 'https://site1.com'></a></html>";
    }
}

class LinkExtractor {
    List<String> extract(String html) {
        List<String> links = new ArrayList<>();
        links.add("https://site1.com");
        links.add("https://site2.com");
        return links;
    }
}

class Main {
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(
                "https://example.com",
                5,
                2
        );
        crawler.start();
    }
}
