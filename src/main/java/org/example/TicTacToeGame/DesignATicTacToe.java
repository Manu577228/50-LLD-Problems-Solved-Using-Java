package org.example.TicTacToeGame;

//Design a Tic-Tac-Toe game where two players take turns placing marks (X and O) on a 3×3 board.
//The system must validate moves, detect win/draw, and declare the result correctly.

import java.util.*;

enum Symbol {
    X, O
}

class Player {
    String name;
    Symbol symbol;

    Player(String name, Symbol symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}

class Move {
    int row, col;

    Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

class Board {
    char[][] grid = new char[3][3];

    Board() {
        for (int i = 0; i < 3; i++)
            Arrays.fill(grid[i], '-');
    }

    boolean placeMove(Move m, Symbol s) {
        if (grid[m.row][m.col] != '-') return false;
        grid[m.row][m.col] = s.name().charAt(0);
        return true;
    }

    boolean checkWin(char c) {
        for (int i = 0; i < 3; i++)
            if ((grid[i][0] == c && grid[i][1] == c && grid[i][2] == c) ||
                    (grid[0][i] == c && grid[1][i] == c && grid[2][i] == c))
                return true;

        return (grid[0][0] == c && grid[1][1] == c && grid[2][2] == c) ||
                (grid[0][2] == c && grid[1][1] == c && grid[2][0] == c);
    }

    boolean isFull() {
        for (char[] r : grid)
            for (char c : r)
                if (c == '-') return false;
        return true;
    }

    void print() {
        for (char[] r : grid)
            System.out.println(Arrays.toString(r));
        System.out.println();
    }
}

class Game {
    Board board = new Board();
    Player p1 = new Player("Bharadwaj1", Symbol.X);
    Player p2 = new Player("Bharadwaj2", Symbol.O);
    Player current = p1;

    void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            board.print(); // display board

            // random / sample input: 0 1, 1 1, 2 2
            System.out.println(current.name + "'s move (row col):");
            int r = sc.nextInt();
            int c = sc.nextInt();

            if (!board.placeMove(new Move(r, c), current.symbol)) {
                System.out.println("Invalid Move");
                continue;
            }

            if (board.checkWin(current.symbol.name().charAt(0))) {
                board.print();
                System.out.println(current.name + " Wins!");
                break;
            }

            if (board.isFull()) {
                board.print();
                System.out.println("Draw!");
                break;
            }

            current = (current == p1) ? p2 : p1; // switch player
        }
    }
}

// Entry point
class Main {
    public static void main(String[] args) {
        new Game().start(); // start game
    }
}