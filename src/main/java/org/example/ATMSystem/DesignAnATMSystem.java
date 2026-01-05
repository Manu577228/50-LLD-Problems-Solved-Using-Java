package org.example.ATMSystem;

//Design an ATM System that allows a customer to authenticate using a card and PIN, perform
//basic banking operations like cash withdrawal, balance inquiry, and deposit, while ensuring
//correctness, security, and extensibility.

class Account {
    private int balance;

    Account(int balance) {
        this.balance = balance;
    }

    int getBalance() {
        return balance;
    }

    boolean debit(int amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    void credit(int amount) {
        balance += amount;
    }
}

class Bank {
    boolean authenticate(String pin) {
        return pin.equals("1234");
    }

    Account getAccount() {
        return new Account(5000);
    }
}

class CashDispenser {
    private int cash = 10000;

    boolean dispense(int amount) {
        if (amount > cash) return false;
        cash -= amount;
        return true;
    }
}

class Screen {
    void show(String msg) {
        System.out.println(msg);
    }
}

class ATM {
    private Bank bank = new Bank();
    private CashDispenser dispenser = new CashDispenser();
    private Screen screen = new Screen();

    void start() {
        screen.show("Enter PIN:");
        String pin = "1234";

        if (!bank.authenticate(pin)) {
            screen.show("Invalid PIN");
            return;
        }

        Account acc = bank.getAccount();
        screen.show("Balance: " + acc.getBalance());

        int withdrawAmount = 2000;
        screen.show("Withdrawing: " + withdrawAmount);

        if (acc.debit(withdrawAmount) && dispenser.dispense(withdrawAmount)) {
            screen.show("Transaction Successful");
        } else {
            screen.show("Transaction Failed");
        }

        screen.show("Remaining Balance: " + acc.getBalance());
    }
}

class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}

