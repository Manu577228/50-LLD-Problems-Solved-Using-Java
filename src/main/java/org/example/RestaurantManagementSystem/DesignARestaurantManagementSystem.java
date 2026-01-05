package org.example.RestaurantManagementSystem;

//Design a Restaurant Management System that manages tables, orders, menu items, and billing.
//The system should allow customers to place orders, kitchen to process them, and generate bills accurately.
//The design must be extensible, scalable, and maintainable.

import java.io.*;
import java.util.*;

enum OrderStatus {
    PLACED, PREPARING, SERVED
}

class MenuItem {
    String name;
    double price;

    MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class OrderItem {
    MenuItem item;
    int quantity;

    OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    double getCost() {
        return item.price * quantity;
    }
}

class Order {
    int orderId;
    List<OrderItem> items = new ArrayList<>();
    OrderStatus status;

    Order(int orderId) {
        this.orderId = orderId;
        this.status = OrderStatus.PLACED;
    }

    void addItem(MenuItem item, int qty) {
        items.add(new OrderItem(item, qty));
    }
}

class Table {
    int tableId;
    List<Order> orders = new ArrayList<>();

    Table(int tableId) {
        this.tableId = tableId;
    }

    void addOrder(Order order) {
        orders.add(order);
    }
}

class Bill {
    Order order;

    Bill(Order order) {
        this.order = order;
    }

    double calculateTotal() {
        double total = 0;
        for (OrderItem oi : order.items) {
            total += oi.getCost();
        }

        return total;
    }
}

class RestaurantSystem {

    public static void main(String[] args) {

        // Create menu
        MenuItem pizza = new MenuItem("Pizza", 250);
        MenuItem burger = new MenuItem("Burger", 120);

        // Create table
        Table table1 = new Table(1);

        // Create order
        Order order1 = new Order(101);
        order1.addItem(pizza, 2);     // 2 pizzas
        order1.addItem(burger, 1);    // 1 burger

        // Assign order to table
        table1.addOrder(order1);

        // Generate bill
        Bill bill = new Bill(order1);

        // Output total
        System.out.println("Table: " + table1.tableId);
        System.out.println("Order ID: " + order1.orderId);
        System.out.println("Total Bill: ₹" + bill.calculateTotal());
    }
}
