package org.example.EventBookingSystem;

//Design an Event Booking System that allows users to browse events,
//check seat availability, book tickets, and make payments.
//The system should prevent overbooking, support multiple events,
//and be easily extensible for future features.

import java.awt.print.Book;
import java.util.UUID;

class User {
    String id;
    String name;

    User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}

class Event {
    String id;
    String name;
    int availableSeats;

    Event(String name, int seats) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.availableSeats = seats;
    }

    synchronized boolean reserveSeats(int count) {
        if (availableSeats < count) return false;
        availableSeats -= count;
        return true;
    }

    synchronized void releaseSeats(int count) {
        availableSeats += count;
    }
}

class Booking {
    String bookingId;
    User user;
    Event event;
    int seats;

    Booking(User user, Event event, int seats) {
        this.bookingId = UUID.randomUUID().toString();
        this.user = user;
        this.event = event;
        this.seats = seats;
    }
}

interface Payment {
    boolean pay(double payment);
}

class CardPayment implements Payment {
    public boolean pay(double amount) {
        return true;
    }
}

class EventManager {
    boolean createBooking(User user, Event event, int seats) {
        if (!event.reserveSeats(seats)) return false;

        Payment payment = new CardPayment();
        if (!payment.pay(seats * 100)) {
            event.releaseSeats(seats);
            return false;
        }

        Booking booking = new Booking(user, event, seats);
        System.out.println("Booking Created: " + booking.bookingId);
        return true;
    }
}

class Main {
    public static void main(String[] args) {

        User u1 = new User("Amit");                 // random user
        Event e1 = new Event("Rock Concert", 50);   // random event

        EventManager manager = new EventManager();  // booking manager
        manager.createBooking(u1, e1, 2);            // random input
    }
}
