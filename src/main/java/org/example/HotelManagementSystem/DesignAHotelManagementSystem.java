package org.example.HotelManagementSystem;

//Design a Hotel Management System that allows managing rooms, guests, bookings, check-in/check-out, and billing.
//The system should track room availability, allocate rooms to guests, and generate bills accurately.

import java.util.*;

enum RoomStatus {
    AVAILABLE, BOOKED
}

enum BookingStatus {
    CONFIRMED, CHECKED_OUT
}

class Room {
    int id;
    String type;
    double price;
    RoomStatus status;

    Room(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.status = RoomStatus.AVAILABLE;
    }
}

class Guest {
    int id;
    String name;

    Guest(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Booking {
    int id;
    Guest guest;
    Room room;
    int days;
    BookingStatus status;

    Booking(int id, Guest guest, Room room, int days) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.days = days;
        this.status = BookingStatus.CONFIRMED;
        room.status = RoomStatus.BOOKED;
    }
}

class Bill {
    static double generate(Booking b) {
        return b.days * b.room.price;
    }
}

class Hotel {
    List<Room> rooms = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();

    void addRoom(Room r) {
        rooms.add(r);
    }

    Room findAvailableRoom(String type) {
        for (Room r : rooms) {
            if (r.type.equals(type) && r.status == RoomStatus.AVAILABLE)
                return r;
        }

        return null;
    }

    Booking bookRoom(Guest g, String type, int days) {
        Room r = findAvailableRoom(type);
        if (r == null) return null;
        Booking b = new Booking(bookings.size() + 1, g, r, days);
        bookings.add(b);
        return b;
    }
}

class Main {
    public static void main(String[] args) {

        Hotel h = new Hotel();                    // create hotel

        h.addRoom(new Room(1, "DELUXE", 3000));   // add room
        h.addRoom(new Room(2, "DELUXE", 3000));   // add room

        Guest g = new Guest(101, "Bharadwaj");        // random guest

        Booking b = h.bookRoom(g, "DELUXE", 2);   // random input

        if (b != null) {
            double amount = Bill.generate(b);    // generate bill
            System.out.println("Booking Done. Amount: " + amount);
        } else {
            System.out.println("No Room Available");
        }
    }
}


