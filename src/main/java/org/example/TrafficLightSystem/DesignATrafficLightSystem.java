package org.example.TrafficLightSystem;

//Design a Traffic Light System that controls traffic at an intersection by switching
//lights (RED, YELLOW, GREEN) in a fixed sequence with configurable durations.
//The system should be extensible, maintainable, and capable of
//supporting multiple intersections in the future.

import java.util.concurrent.TimeUnit;

enum SignalState {
    RED, GREEN, YELLOW
}

class TrafficLight {
    private SignalState state;
    private int duration;

    TrafficLight(SignalState state, int duration) {
        this.state = state;
        this.duration = duration;
    }

    SignalState getState() {
        return state;
    }

    void setState(SignalState state) {
        this.state = state;
    }

    int getDuration() {
        return duration;
    }
}

class TrafficController {
    void changeState(TrafficLight light) throws InterruptedException {
        System.out.printf("Signal: " + light.getState() + "\n");

        TimeUnit.SECONDS.sleep(light.getDuration());

        if (light.getState() == SignalState.RED)
            light.setState(SignalState.GREEN);
        else if (light.getState() == SignalState.GREEN)
            light.setState(SignalState.YELLOW);
        else
            light.setState(SignalState.RED);
    }
}

class Intersection {
    private TrafficLight light;
    private TrafficController controller;

    Intersection(TrafficLight light) {
        this.light = light;
        this.controller = new TrafficController();
    }

    void start(int cycles) throws InterruptedException {
        for (int i = 0; i < cycles; i++) {
            controller.changeState(light);
        }
    }
}

class Main {

    public static void main(String[] args) throws Exception {

        // Random Input Example:
        // Initial State = RED, Duration = 3 seconds, Cycles = 6

        TrafficLight light = new TrafficLight(SignalState.RED, 3);

        Intersection intersection = new Intersection(light);

        intersection.start(6);
    }
}