package org.example.SnakeAndLadder;

//Design a Snake & Ladder game where multiple players take turns rolling a dice,
//move on a board from start to end, climb ladders, slide down snakes,
//and the first player to reach the final cell wins.

import java.util.*;

class Player {
    String name;
    int position;

    Player(String name) {
        this.name = name;
        this.position = 0;
    }
}

class Dice {
    Random random = new Random();

    int roll() {
        return random.nextInt(6) + 1;
    }
}

class Board {
    Map<Integer, Integer> jumps = new HashMap<>();

    Board() {
        jumps.put(2, 38);
        jumps.put(7, 14);
        jumps.put(16, 6);
        jumps.put(36, 8);
        jumps.put(51, 67);
        jumps.put(98, 78);
    }

    int getNextPosition(int pos) {
        return jumps.getOrDefault(pos, pos);
    }
}

class Game {
    Queue<Player> players = new LinkedList<>();
    Board board = new Board();
    Dice dice = new Dice();
    final int WIN_POSITION = 100;

    Game(List<Player> playerList) {
        players.addAll(playerList);
    }

    void start() {
        while (true) {
            Player current = players.poll();
            int roll = dice.roll();
            int nextPos = current.position + roll;

            if (nextPos <= WIN_POSITION) {
                current.position = board.getNextPosition(nextPos);
            }

            System.out.println(current.name + " rolled " + roll + " moved to " + current.position);

            if (current.position == WIN_POSITION) {
                System.out.println(current.name + " WINS!");
                break;
            }

            players.offer(current);
        }
    }
}

class SnakeLadderGame {
    public static void main(String[] args) {
        List<Player> players = Arrays.asList(
                new Player("A"),
                new Player("B")
        );

        Game game = new Game(players); // Create game
        game.start();                  // Start game
    }
}
