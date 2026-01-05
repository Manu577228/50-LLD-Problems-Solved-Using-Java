package org.example.FeatureFlagSystem;

//A Feature Flag System allows enabling or disabling application
//features dynamically at runtime without redeploying code.
//It supports gradual rollouts, A/B testing, and instant
//rollback of features for specific users or environments.

import java.util.*;

class User {
    String userId;

    User(String userId) {
        this.userId = userId;
    }
}

class FeatureContext {
    User user;
    String environment;

    FeatureContext(User user, String environment) {
        this.user = user;
        this.environment = environment;
    }
}

interface Rule {
    boolean evaluate(FeatureContext ctx);
}

class UserRule implements Rule {
    Set<String> allowedUsers;

    UserRule(Set<String> users) {
        this.allowedUsers = users;
    }

    public boolean evaluate(FeatureContext ctx) {
        return allowedUsers.contains(ctx.user.userId);
    }
}

class PercentageRule implements Rule {
    int percentage;

    PercentageRule(int percentage) {
        this.percentage = percentage;
    }

    public boolean evaluate(FeatureContext ctx) {
        int hash = Math.abs(ctx.user.userId.hashCode()) % 100;
        return hash < percentage;
    }
}

class FeatureFlag {
    String name;
    boolean enabled;
    List<Rule> rules;

    FeatureFlag(String name, boolean enabled, List<Rule> rules) {
        this.name = name;
        this.enabled = enabled;
        this.rules = rules;
    }
}

class FeatureManager {
    Map<String, FeatureFlag> flags = new HashMap<>();

    void addFeature(FeatureFlag flag) {
        flags.put(flag.name, flag);
    }

    boolean isEnabled(String featureName, FeatureContext ctx) {
        FeatureFlag flag = flags.get(featureName);
        if (flag == null || !flag.enabled) return false;

        for (Rule rule : flag.rules) {
            if (rule.evaluate(ctx)) return true;
        }

        return false;
    }
}

class Main {
    public static void main(String[] args) {

        User user = new User("user123");                 // Random user
        FeatureContext ctx = new FeatureContext(user, "PROD");

        Rule userRule = new UserRule(Set.of("user123")); // User based rule
        Rule percentRule = new PercentageRule(50);       // 50% rollout

        FeatureFlag checkout = new FeatureFlag(
                "NEW_CHECKOUT",
                true,
                List.of(userRule, percentRule)
        );

        FeatureManager manager = new FeatureManager();
        manager.addFeature(checkout);

        System.out.println(
                manager.isEnabled("NEW_CHECKOUT", ctx)       // Output: true
        );
    }
}
