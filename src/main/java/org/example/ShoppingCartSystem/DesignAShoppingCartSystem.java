package org.example.ShoppingCartSystem;

//Design a Shopping Cart System that allows users to add, remove, and
//update products in a cart and calculate the final payable amount.
//The system should support multiple users, handle quantities, and
//be easily extensible for pricing rules like discounts or taxes.

import java.util.*;

class Product {
    private final int id;
    private final String name;
    private final double price;

    Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    double getPrice() {
        return price;
    }

    String getName() {
        return name;
    }

    int getId() {
        return id;
    }
}

class CartItem {
    private final Product product;
    private int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    int getProductId() {
        return product.getId();
    }

    int getQuantity() {
        return quantity;
    }

    String getProductName() {
        return product.getName();
    }
}

class Cart {
    private final Map<Integer, CartItem> items = new HashMap<>();

    void addProduct(Product product, int quantity) {
        if (items.containsKey(product.getId())) {
            CartItem item = items.get(product.getId());
            item.updateQuantity(item.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }

    void removeProduct(int productId) {
        items.remove(productId);
    }

    double calculateTotal() {
        double total = 0;

        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }

        return total;
    }

    void viewCart() {
        for (CartItem item : items.values()) {
            System.out.println(item.getProductName() + " x " + item.getQuantity());
        }
    }
}

class User {
    private final Cart cart = new Cart();

    Cart getCart() {
        return cart;
    }
}

class Main {

    public static void main(String[] args) {

        // Creating products
        Product p1 = new Product(1, "Laptop", 50000);
        Product p2 = new Product(2, "Mouse", 500);

        // Creating user
        User user = new User();

        // Adding products to cart
        user.getCart().addProduct(p1, 1);
        user.getCart().addProduct(p2, 2);

        // Viewing cart
        user.getCart().viewCart();

        // Printing total price
        System.out.println("Total = " + user.getCart().calculateTotal());
    }
}