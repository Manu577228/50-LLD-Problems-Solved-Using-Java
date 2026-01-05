package org.example.LibraryManagementSystem;
//Design a Library Management System that allows users to search, issue, return, and
//reserve books, while librarians manage book inventory and user records.
//The system should track book availability, borrowing history, and fine calculation.

import java.util.*;

abstract class User {
    String id;
    String name;

    User(String id, String name){
        this.id = id;
        this.name = name;
    }
}

class Member extends User {
    List<Loan> loans = new ArrayList<>();

    Member(String id, String name){
        super(id, name);
    }
}

class Librarian extends User {
    Librarian(String id, String name){
        super(id, name);
    }
}

class Book {
    String isbn;
    String title;
    String author;

    Book(String isbn, String title, String author){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
}

class BookCopy {
    Book book;
    boolean isAvailable = true;

    BookCopy(Book book){
        this.book = book;
    }
}

class Loan {
    Member member;
    BookCopy copy;
    Date issueDate;

    Loan(Member member, BookCopy copy){
        this.member = member;
        this.copy = copy;
        this.issueDate = new Date();
    }
}

class Catalog {
    List<BookCopy> copies;

    Catalog(List<BookCopy> copies){
        this.copies = copies;
    }

    BookCopy searchByTitle(String title){
        for(BookCopy c : copies){
            if(c.book.title.equalsIgnoreCase(title) && c.isAvailable)
                return c;
        }
        return null;
    }
}

class Library {
    private static Library instance;
    List<BookCopy> copies = new ArrayList<>();
    List<Loan> loans = new ArrayList<>();
    Catalog catalog = new Catalog(copies);

    private Library(){}

    static Library getInstance(){
        if(instance == null)
            instance = new Library();
        return instance;
    }

    void addBook(Book book){
        copies.add(new BookCopy(book));
        System.out.println("Book added: " + book.title);
    }

    void issueBook(Member m, String title){
        BookCopy copy = catalog.searchByTitle(title);
        if(copy == null){
            System.out.println("Book not available");
            return;
        }

        copy.isAvailable = false;
        Loan loan = new Loan(m, copy);
        loans.add(loan);
        m.loans.add(loan);

        System.out.println("Book issued to " + m.name);
    }

    void returnBook(Member m, BookCopy copy){
        Loan target = null;

        for(Loan l : loans){
            if(l.member == m && l.copy == copy){
                target = l;
                break;
            }
        }

        if(target == null){
            System.out.println("No active loan found");
            return;
        }

        copy.isAvailable = true;
        loans.remove(target);
        m.loans.remove(target);

        System.out.println("Book returned by " + m.name);
    }
}

class Example1 {
    public static void main(String[] args) {

        Library lib = Library.getInstance();

        Book b1 = new Book("101", "Clean Code", "Robert Martin");
        lib.addBook(b1);

        Member m1 = new Member("1", "Bharadwaj");

        lib.issueBook(m1, "Clean Code");

        BookCopy copy = lib.copies.get(0);
        lib.returnBook(m1, copy);
    }
}

