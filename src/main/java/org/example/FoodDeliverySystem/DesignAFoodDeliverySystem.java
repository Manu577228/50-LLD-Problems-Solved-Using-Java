package org.example.FoodDeliverySystem;

//Design a Food Delivery System where users can browse restaurants, place food orders,
//make payments, and get orders delivered by delivery partners.
//The system should manage users, restaurants, orders, payments, and
//delivery flow in a scalable and extensible way.

import java.util.*;

enum OrderStatus {
    CREATED,
    PAID,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED
}

interface PaymentMethod {
    boolean pay(double amount);
}

class CardPayment implements PaymentMethod {
    public boolean pay(double amount) {
        return true;
    }
}

class MenuItem {
    String name;
    double price;

    MenuItem(String n, double p) {
        name = n;
        price = p;
    }
}

class Restaurant {
    String name;
    List<MenuItem> menu = new ArrayList<>();

    Restaurant(String n) {
        name = n;
    }

    void addItem(MenuItem item) {
        menu.add(item);
    }
}

class DeliveryPartner {
    String name;

    DeliveryPartner(String n) {
        name = n;
    }
}

class Order {
    List<MenuItem> items;
    OrderStatus status;
    PaymentMethod payment;
    DeliveryPartner partner;

    Order(List<MenuItem> i) {
        items = i;
        status = OrderStatus.CREATED;
    }

    double getTotal() {
        double sum = 0;
        for (MenuItem m : items) sum += m.price;
        return sum;
    }

    void makePayment(PaymentMethod p) {
        payment = p;

        if (payment.pay(getTotal()))
            status = OrderStatus.PAID;
    }

    void assignDelivery(DeliveryPartner d) {
        partner = d;
        status = OrderStatus.OUT_FOR_DELIVERY;
    }
}

class User {
    String name;

    User(String n) {
        name = n;
    }

    Order placeOrder(List<MenuItem> items) {
        return new Order(items);
    }
}

class Main {
    public static void main(String[] args) {

        // Create user
        User user = new User("Bharadwaj");

        // Create restaurant and menu
        Restaurant r = new Restaurant("PizzaHub");
        r.addItem(new MenuItem("Pizza", 250));
        r.addItem(new MenuItem("Burger", 150));

        // Random input: user selects items
        List<MenuItem> cart = new ArrayList<>();
        cart.add(r.menu.get(0));
        cart.add(r.menu.get(1));

        // Place order
        Order order = user.placeOrder(cart);

        // Make payment
        order.makePayment(new CardPayment());

        // Assign delivery
        order.assignDelivery(new DeliveryPartner("Rahul"));

        // Final status output
        System.out.println("Order Status: " + order.status);
        System.out.println("Total Amount: " + order.getTotal());
    }
}


