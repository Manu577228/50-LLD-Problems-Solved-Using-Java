package org.example.RecommendationSystem;

//Design a system that recommends items (movies/products/content) to
//users based on their past interactions and preferences.
//The system should support multiple recommendation strategies and allow easy future extensions.

import java.util.*;

class User {
    int id;
    String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Item {
    int id;
    String category;

    Item(int id, String category) {
        this.id = id;
        this.category = category;
    }
}

class Interaction {
    int userId;
    int itemId;
    String type;

    Interaction(int userId, int itemId, String type) {
        this.userId = userId;
        this.itemId = itemId;
        this.type = type;
    }
}

interface RecommendationStrategy {
    List<Item> recommend(
            User user,
            List<Item> items,
            List<Interaction> interactions
    );
}

class BasicRecommendationStrategy implements RecommendationStrategy {
    public List<Item> recommend(
            User user,
            List<Item> items,
            List<Interaction> interactions
    ) {
        Set<Integer> seen = new HashSet<>();

        for (Interaction i : interactions) {
            if (i.userId == user.id) {
                seen.add(i.itemId);
            }
        }

        List<Item> result = new ArrayList<>();

        for (Item item : items) {
            if (!seen.contains(item.id)) {
                result.add(item);
            }
        }

        return result;
    }
}

class RecommendationService {
    RecommendationStrategy strategy;

    RecommendationService(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    List<Item> getRecommendations(
            User user,
            List<Item> items,
            List<Interaction> interactions
    ) {
        return strategy.recommend(user, items, interactions);
    }
}

class Main {
    public static void main(String[] args) {

        User user = new User(1, "Alex");         // Random user

        List<Item> items = Arrays.asList(        // Random items
                new Item(1, "Action"),
                new Item(2, "Drama"),
                new Item(3, "Comedy")
        );

        List<Interaction> interactions = Arrays.asList(
                new Interaction(1, 1, "VIEW"),       // User viewed item 1
                new Interaction(1, 2, "LIKE")        // User liked item 2
        );

        RecommendationStrategy strategy =
                new BasicRecommendationStrategy();   // Choose strategy

        RecommendationService service =
                new RecommendationService(strategy); // Create service

        List<Item> recs =
                service.getRecommendations(user, items, interactions);

        for (Item i : recs) {                    // Print results
            System.out.println(i.category);
        }
    }
}