package org.example.ElevatorSystem;//Design a system to control multiple elevators in a building where users request elevators
//from floors and select destination floors inside the elevator.
//The system must efficiently assign elevators, move them correctly, and handle multiple requests.

import java.util.*;

enum Direction {
    UP, DOWN
}

enum ElevatorState {
    IDLE, MOVINGUP, MOVINGDOWN
}

class Request {
    int floor;
    Direction direction;

    Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}

class Elevators {
    int id;
    int currentFloor;
    ElevatorState state;
    TreeSet<Integer> targets;

    Elevators(int id) {
        this.id = id;
        this.currentFloor = 0;
        this.state = ElevatorState.IDLE;
        this.targets = new TreeSet<>();
    }

    void addTarget(int floor) {
        targets.add(floor);
    }

    void move() {
        if (targets.isEmpty()) {
            state = ElevatorState.IDLE;
            return;
        }

        int target = targets.first();

        if (currentFloor < target) {
            state = ElevatorState.MOVINGUP;
            currentFloor++;
        } else if (currentFloor > target) {
            state = ElevatorState.MOVINGDOWN;
            currentFloor--;
        }

        if (currentFloor == target) {
            targets.remove(target);
        }

        System.out.println("Elevator " + id + " at floor " + currentFloor + " state " + state);
    }
}

class ElevatorController {
    Elevators[] elevators;

    ElevatorController(int count) {
        elevators = new Elevators[count];
        for (int i = 0; i < count; i++) {
            elevators[i] = new Elevators(i);
        }
    }

    Elevators assignRequest(Request r) {
        Elevators best = elevators[0];

        int minDistance = Math.abs(best.currentFloor - r.floor);

        for (Elevators e : elevators) {
            int dist = Math.abs(e.currentFloor - r.floor);

            if (dist < minDistance) {
                minDistance = dist;
                best = e;
            }
        }

        best.addTarget(r.floor);
        return best;
    }

    void step() {
        for (Elevators e : elevators) {
            e.move();
        }
    }
}

class ElevatorSystem {
    public static void main(String[] args) {

        ElevatorController controller = new ElevatorController(2);
        // Creates elevator controller with 2 elevators

        controller.assignRequest(new Request(5, Direction.UP));
        // External request from floor 5 going UP

        controller.assignRequest(new Request(2, Direction.DOWN));
        // External request from floor 2 going DOWN

        for (int i = 0; i < 10; i++) {
            // Simulates time steps in the system
            controller.step();
        }
    }
}