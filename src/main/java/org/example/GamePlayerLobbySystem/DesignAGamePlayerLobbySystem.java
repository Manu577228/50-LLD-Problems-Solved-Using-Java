package org.example.GamePlayerLobbySystem;

//Design a Game Player Lobby System where players can join a lobby, leave it,
//and get matched into a game when lobby conditions are met.
//The system should manage players, lobbies, and matchmaking in a clean, scalable way.

import java.util.*;

class Player {
    private final int id;
    private final String name;

    Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    String getName() {
        return name;
    }
}

class Lobby {
    private final int id;
    private final int capacity;
    private final List<Player> players = new ArrayList<>();

    Lobby(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    boolean addPlayer(Player p) {
        if (players.size() >= capacity) return false;
        players.add(p);
        return true;
    }

    void removePlayer(Player p) {
        players.remove(p);
    }

    boolean isFull() {
        return players.size() == capacity;
    }

    List<Player> getPlayers() {
        return players;
    }
}

class Game {
    private final List<Player> players;

    Game(List<Player> players) {
        this.players = players;
    }

    void start() {
        System.out.println("Game started with players:");
        for (Player p : players) {
            System.out.println(p.getName());
        }
    }
}

class LobbyManager {
    private final List<Lobby> lobbies = new ArrayList<>();

    void join(Player p) {
        for (Lobby l : lobbies) {
            if (l.addPlayer(p)) {
                if (l.isFull()) {
                    Game g = new Game(new ArrayList<>(l.getPlayers()));
                    g.start();
                    lobbies.remove(l);
                }

                return;
            }
        }

        Lobby newLobby = new Lobby(lobbies.size() + 1, 2);
        newLobby.addPlayer(p);
        lobbies.add(newLobby);
    }
}

class Main {

    public static void main(String[] args) {

        /* create lobby manager */
        LobbyManager manager = new LobbyManager();

        /* random input players */
        Player p1 = new Player(1, "Alice");
        Player p2 = new Player(2, "Bob");
        Player p3 = new Player(3, "Charlie");

        /* players join */
        manager.join(p1);
        manager.join(p2);
        manager.join(p3);
    }
}

