//Design a system that converts a long URL into a short unique URL and later
//redirects the short URL back to the original long URL.
//The system must be fast, reliable, and scalable to handle millions of requests.

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class UrlMapping {
    String longUrl;
    String shortUrl;

    UrlMapping(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }
}

class UrlRepository {
    private static UrlRepository instance = new UrlRepository();
    private Map<String, UrlMapping> store = new HashMap<>();

    private UrlRepository() {
    }

    static UrlRepository getInstance() {
        return instance;
    }

    void save(UrlMapping mapping) {
        store.put(mapping.shortUrl, mapping);
    }

    UrlMapping find(String shortUrl) {
        return store.get(shortUrl);
    }
}

class KeyGenerator {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private AtomicLong counter = new AtomicLong(1);

    String generateKey() {
        long id = counter.getAndIncrement();
        StringBuilder key = new StringBuilder();

        while (id > 0) {
            key.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }

        return key.toString();
    }
}


class UrlShortenerService {
    private UrlRepository repo = UrlRepository.getInstance();
    private KeyGenerator generator = new KeyGenerator();

    String createShortUrl(String longUrl) {
        String key = generator.generateKey();
        String shortUrl = "http://short.ly" + key;
        repo.save(new UrlMapping(longUrl, shortUrl));
        return shortUrl;
    }

    String getLongUrl(String shortUrl) {
        UrlMapping mapping = repo.find(shortUrl);
        return mapping == null ? null : mapping.longUrl;
    }
}

class UrlController {
    private UrlShortenerService service = new UrlShortenerService();

    String shortenUrl(String longUrl) {
        return service.createShortUrl(longUrl);
    }

    String redirect(String shortUrl) {
        return service.getLongUrl(shortUrl);
    }
}

class Main {
    public static void main(String[] args) {
        UrlController controller = new UrlController(); // Controller instance

        String longUrl = "https://www.google.com/search?q=system+design"; // Input
        String shortUrl = controller.shortenUrl(longUrl);                 // Shorten

        System.out.println("Short URL: " + shortUrl);                     // Output
        System.out.println("Redirects To: " + controller.redirect(shortUrl));
    }
}