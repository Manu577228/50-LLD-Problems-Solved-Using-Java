package org.example.CarRentalSystem;//Design a Car Rental System that allows customers to search available cars, book cars
//for a duration, calculate rental cost, and manage bookings.
//The system should support multiple car types, locations, and booking lifecycle management.

import java.util.*;

enum CarType {
    SEDAN, SUV, HATCHBACK
}

class User {
    int id;
    String name;

    User(int id, String name){
        this.id = id;
        this.name = name;
    }
}

class Car {
    int id;
    CarType type;
    int pricePerDay;
    boolean available;

    Car(int id, CarType type, int pricePerDay){
        this.id = id;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.available = true;
    }
}

class Booking {
    int id;
    User user;
    Car car;
    int days;
    int totalCost;

    Booking(int id, User user, Car car, int days){
        this.id = id;
        this.user = user;
        this.car = car;
        this.days = days;
        this.totalCost = car.pricePerDay * days;
    }
}

class RentalService {
    List<Car> cars = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();
    int bookingCounter = 1;

    void addCar(Car car){
        cars.add(car);
    }

    List<Car> search(CarType type){
        List<Car> result = new ArrayList<>();
        for(Car car : cars){
            if(car.available && car.type == type){
                result.add(car);
            }
        }

        return result;
    }

    Booking bookCar(User user, Car car, int days){
        if(!car.available) return null;
        car.available = false;
        Booking booking = new Booking(bookingCounter++, user, car, days);
        bookings.add(booking);
        return booking;
    }

    void cancelBooking(Booking booking){
        booking.car.available = true;
        bookings.remove(booking);
    }
}

class Example {
    public static void main(String[] args) {

        RentalService service = new RentalService(); // Create service

        // Add cars
        service.addCar(new Car(1, CarType.SEDAN, 2000));
        service.addCar(new Car(2, CarType.SUV, 3000));

        // Create user
        User user = new User(101, "Bharadwaj");

        // Search cars
        List<Car> sedans = service.search(CarType.SEDAN);

        // Random Input Example
        // User books Sedan for 3 days
        Booking booking = service.bookCar(user, sedans.get(0), 3);

        // Output
        System.out.println("Booked Car ID: " + booking.car.id);
        System.out.println("Total Cost: " + booking.totalCost);
    }
}


