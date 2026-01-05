package org.example.DigitalWalletSystem;

//Design a Digital Wallet system that allows users to add money, transfer money, and check balance securely.
//The system should manage users, wallet balances, and transactions in a scalable and extensible manner.

import java.util.*;

class User {
    private final int id;
    private final String name;
    private final Wallet wallet;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.wallet = new Wallet();
    }

    public Wallet getWallet() {
        return wallet;
    }

    public int getId() {
        return id;
    }
}

class Wallet {
    private int balance = 0;

    public synchronized void credit(int amount) {
        balance += amount;
    }

    public synchronized boolean debit(int amount) {
        if (balance < amount) return false;
        balance -= amount;
        return true;
    }

    public int getBalance() {
        return balance;
    }
}

class Transaction {
    int fromUserId;
    int toUserId;
    int amount;
    long timestamp;

    public Transaction(int from, int to, int amount) {
        this.fromUserId = from;
        this.toUserId = to;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }
}

class WalletService {
    private final Map<Integer, User> users = new HashMap<>();

    private final List<Transaction> transactions = new ArrayList<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void addMoney(int userId, int amount) {
        users.get(userId).getWallet().credit(amount);
    }

    public boolean transfer(int fromUser, int toUser, int amount) {
        Wallet senderWallet = users.get(fromUser).getWallet();
        Wallet receiveWallet = users.get(toUser).getWallet();

        if (!senderWallet.debit(amount)) {
            return false;
        }

        receiveWallet.credit(amount);

        transactions.add(new Transaction(fromUser, toUser, amount));
        return true;
    }
}

class Main {

    public static void main(String[] args) {

        WalletService service = new WalletService();

        // Create users
        User u1 = new User(1, "Alice");
        User u2 = new User(2, "Bob");

        // Register users
        service.addUser(u1);
        service.addUser(u2);

        // Add money to Alice
        service.addMoney(1, 1000);

        // Transfer money from Alice to Bob
        service.transfer(1, 2, 300);

        // Print balances
        System.out.println("Alice Balance: " + u1.getWallet().getBalance());
        System.out.println("Bob Balance: " + u2.getWallet().getBalance());
    }
}

