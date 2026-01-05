package org.example.AuctionSystem;

//Design an Auction System where sellers can create auctions for items, buyers
//can place bids, and the system determines the winner when the auction ends.
//The system must support multiple auctions, multiple bidders, and real-time highest bid tracking.

import java.util.*;

class User {
    int id;
    String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Item {
    int id;
    String nam;

    Item(int id, String name) {
        this.id = id;
        this.nam = name;
    }
}

class Bid {
    User bidder;
    double amount;
    long time;

    Bid(User bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
        this.time = System.currentTimeMillis();
    }
}

class Auction {
    int id;
    Item item;
    User seller;
    double startPrice;
    List<Bid> bids = new ArrayList<>();
    Bid highestBid;
    long endTime;

    Auction(int id, Item item, User seller, double startPrice, long durationMs) {
        this.id = id;
        this.item = item;
        this.seller = seller;
        this.startPrice = startPrice;
        this.endTime = System.currentTimeMillis() + durationMs;
    }

    synchronized boolean placeBid(Bid bid) {
        if (System.currentTimeMillis() > endTime) return false;

        if (highestBid == null && bid.amount >= startPrice) {
            highestBid = bid;
        } else if (highestBid != null && bid.amount > highestBid.amount) {
            highestBid = bid;
        } else {
            return false;
        }

        bids.add(bid);
        return true;
    }

    Bid getWinner() {
        return highestBid;
    }
}

class AuctionManager {
    private static AuctionManager instance;
    Map<Integer, Auction> auctions = new HashMap<>();

    private AuctionManager() {
    }

    static AuctionManager getInstance() {
        if (instance == null) instance = new AuctionManager();
        return instance;
    }

    Auction createAuction(int id, Item item, User seller, double price, long duration) {
        Auction auction = new Auction(id, item, seller, price, duration);
        auctions.put(id, auction);
        return auction;
    }
}

class Main {
    public static void main(String[] args) {

        User seller = new User(1, "Alice");      // Seller
        User bidder1 = new User(2, "Bob");       // Bidder 1
        User bidder2 = new User(3, "Charlie");   // Bidder 2

        Item item = new Item(101, "iPhone");     // Auction item

        AuctionManager manager = AuctionManager.getInstance();

        Auction auction = manager.createAuction(
                1, item, seller, 500, 60000
        );

        auction.placeBid(new Bid(bidder1, 550)); // Bob bids
        auction.placeBid(new Bid(bidder2, 650)); // Charlie bids

        Bid winner = auction.getWinner();

        System.out.println(
                "Winner: " + winner.bidder.name +
                        " Amount: " + winner.amount
        );
    }
}

/*
 Sample Input:
  Bob bids 550
  Charlie bids 650

 Output:
  Winner: Charlie Amount: 650
*/