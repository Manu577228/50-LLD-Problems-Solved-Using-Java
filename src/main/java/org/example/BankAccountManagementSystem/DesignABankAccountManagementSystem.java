package org.example.BankAccountManagementSystem;

//Design a system to manage bank accounts where customers can open accounts,
//deposit money, withdraw money, and check balance.
//The system must support multiple account types, ensure data consistency, and be easy to extend.

import java.util.*;

abstract class Account {
    protected long accountNumber;
    protected double balance;

    Account(long accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    void deposit(double amount) {
        balance += amount;
    }

    abstract void withdraw(double amount);

    double getBalance() {
        return balance;
    }
}

class SavingsAccount extends Account {
    SavingsAccount(long accountNumber) {
        super(accountNumber);
    }

    void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds!");
        }
    }
}

class CurrentAccount extends Account {
    CurrentAccount(long accountNumber) {
        super(accountNumber);
    }

    void withdraw(double amount) {
        balance -= amount;
    }
}

class Bank {
    private Map<Long, Account> accounts = new HashMap<>();

    void addAccount(Account account) {
        accounts.put(account.accountNumber, account);
    }

    Account getAccount(long accNo) {
        return accounts.get(accNo);
    }
}

class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();                             // Create bank

        Account acc1 = new SavingsAccount(101);             // Create savings account
        Account acc2 = new CurrentAccount(102);             // Create current account

        bank.addAccount(acc1);                              // Add accounts to bank
        bank.addAccount(acc2);

        acc1.deposit(5000);                                 // Deposit money
        acc1.withdraw(2000);                                // Withdraw money

        acc2.deposit(3000);                                 // Deposit
        acc2.withdraw(5000);                                // Overdraft allowed

        System.out.println(acc1.getBalance());              // Output: 3000
        System.out.println(acc2.getBalance());              // Output: -2000
    }
}