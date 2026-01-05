package org.example.ChessTournamentSystem;

//Design a system to manage a chess tournament where players are registered,
//matches are scheduled, results are recorded, and standings are generated.
//The system should support multiple rounds, fair pairings, and result tracking in a clean, extensible way.

import java.util.*;

class Player {
    int id;
    String name;
    int score;

    Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
    }

    void addScore(int points) {
        score += points;
    }
}

class Match {
    Player p1;
    Player p2;
    Player winner;

    Match(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    void playMatch() {
        winner = p1;
        winner.addScore(1);
    }
}

interface PairingStrategy {
    List<Match> createMatches(List<Player> players);
}

class SequentialPairingStrategy implements PairingStrategy {
    public List<Match> createMatches(List<Player> players) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < players.size(); i += 2) {
            matches.add(new Match(players.get(i), players.get(i + 1)));
        }

        return matches;
    }
}

class Round {
    List<Match> matches = new ArrayList<>();

    void addMatches(List<Match> m) {
        matches.addAll(m);
    }

    void playRound() {
        for (Match m : matches) {
            m.playMatch();
        }
    }
}

class Tournament {
    List<Player> players;
    List<Round> rounds = new ArrayList<>();
    PairingStrategy strategy;

    Tournament(List<Player> players, PairingStrategy strategy) {
        this.players = players;
        this.strategy = strategy;
    }

    void startTournament() {
        Round round = new Round();
        round.addMatches(strategy.createMatches(players));
        round.playRound();
        rounds.add(round);
    }

    void displayStandings() {
        players.sort((a, b) -> b.score - a.score);

        for (Player p : players) {
            System.out.println(p.name + " : " + p.score);
        }
    }
}

class Main {
    public static void main(String[] args) {

        // Sample input players
        List<Player> players = Arrays.asList(
                new Player(1, "A"),
                new Player(2, "B"),
                new Player(3, "C"),
                new Player(4, "D")
        );

        Tournament t = new Tournament(players, new SequentialPairingStrategy());
        t.startTournament();
        t.displayStandings();
    }
}
