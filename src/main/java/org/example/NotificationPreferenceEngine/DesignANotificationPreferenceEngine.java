package org.example.NotificationPreferenceEngine;

//Design a system that allows users to configure notification preferences (channel, type, frequency).
//The system should decide whether a notification should be sent based on these preferences.
//It must be extensible to support new notification channels and rules.

import java.util.*;

enum NotificationChannel {
    EMAIL, SMS, PUSH
}

enum NotificationType {
    PROMOTIONAL, TRANSACTIONAL, ALERT
}

class User {
    String id;

    User(String id) {
        this.id = id;
    }
}

class Notification {
    NotificationType type;
    NotificationChannel channel;

    Notification(NotificationType type, NotificationChannel channel) {
        this.type = type;
        this.channel = channel;
    }
}

class NotificationPreference {
    boolean enabled;
    Set<NotificationType> allowedTypes;
    Set<NotificationChannel> allowedChannels;

    NotificationPreference(boolean enabled, Set<NotificationType> types, Set<NotificationChannel> channels) {
        this.enabled = enabled;
        this.allowedTypes = types;
        this.allowedChannels = channels;
    }
}

interface ChannelPolicy {
    boolean isAllowed(Notification notification);
}

class EmailPolicy implements ChannelPolicy {
    public boolean isAllowed(Notification n) {
        return true;
    }
}

class SMSPolicy implements ChannelPolicy {
    public boolean isAllowed(Notification n) {
        return n.type != NotificationType.PROMOTIONAL;
    }
}

class ChannelPolicyFactory {
    static ChannelPolicy getPolicy(NotificationChannel c) {
        if (c == NotificationChannel.EMAIL) return new EmailPolicy();
        if (c == NotificationChannel.SMS) return new SMSPolicy();
        return n -> true;
    }
}

class PreferenceEngine {
    boolean shouldNotify(User u, Notification n, NotificationPreference p) {
        if (!p.enabled) return false;
        if (!p.allowedTypes.contains(n.type)) return false;
        if (!p.allowedChannels.contains(n.channel)) return false;

        ChannelPolicy policy = ChannelPolicyFactory.getPolicy(n.channel);

        return policy.isAllowed(n);
    }
}

class NotificationService {
    PreferenceEngine engine = new PreferenceEngine();

    void process(User u, Notification n, NotificationPreference p) {
        boolean send = engine.shouldNotify(u, n, p);
        System.out.println("Send Notification: " + send);
    }
}

class Main {
    public static void main(String[] args) {

        User u = new User("U1");

        Notification n =
                new Notification(NotificationType.PROMOTIONAL,
                        NotificationChannel.SMS);

        NotificationPreference p =
                new NotificationPreference(
                        true,
                        Set.of(NotificationType.ALERT, NotificationType.TRANSACTIONAL),
                        Set.of(NotificationChannel.EMAIL, NotificationChannel.SMS)
                );

        new NotificationService().process(u, n, p);
    }
}
