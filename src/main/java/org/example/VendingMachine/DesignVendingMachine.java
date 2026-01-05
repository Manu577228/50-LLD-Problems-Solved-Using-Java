package org.example.VendingMachine;//Design a vending machine that allows users to select products,
//        insert money, dispense items, and return change.
//The system should handle inventory management, payments, and basic error cases like
//insufficient funds or out-of-stock items.

import java.util.*;

class Product {
    int id;
    String name;
    int price;

    Product(int id, String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Inventory {
    Map<Integer, Integer> stock = new HashMap<>();
    Map<Integer, Product> products = new HashMap<>();

    void addProduct(Product p, int qty){
        products.put(p.id, p);
        stock.put(p.id, qty);
    }

    boolean isAvailable(int id){
        return stock.getOrDefault(id, 0) > 0;
    }

    void deduct(int id){
        stock.put(id, stock.get(id) - 1);
    }

    Product getProduct(int id){
        return products.get(id);
    }
}

class Payment {
    int balance = 0;

    void insert(int amount){
        balance += amount;
    }

    void reset(){
        balance = 0;
    }
}

class VendingMachine {
    Inventory inventory = new Inventory();
    Payment payment = new Payment();

    void selectProduct(int productId){
        if(!inventory.isAvailable(productId)){
            System.out.println("Out of Stock");
            return;
        }

        Product product = inventory.getProduct(productId);

        if(payment.balance < product.price){
            System.out.println("Insufficient balance");
            return;
        }

        inventory.deduct(productId);
        payment.balance -= product.price;

        System.out.println("Dispensed: " + product.name);
        System.out.println("Change Returned: " + payment.balance);

        payment.reset();
    }

    void insertMoney(int amount){
        payment.insert(amount);
        System.out.println("Inserted: " + amount);
    }
}

class Main {
    public static void main(String[] args) {

        VendingMachine vm = new VendingMachine();

        // Creating products
        Product coke = new Product(1, "Coke", 25);
        Product chips = new Product(2, "Chips", 15);

        // Adding products to inventory
        vm.inventory.addProduct(coke, 5);
        vm.inventory.addProduct(chips, 3);

        // Random input simulation
        vm.insertMoney(30);        // User inserts 30
        vm.selectProduct(1);       // User selects Coke
    }
}


