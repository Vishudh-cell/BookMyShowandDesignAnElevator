package elevator;

import java.util.*;

// ENUM
enum Direction { UP, DOWN, IDLE }

// REQUEST
class Request {
    int floor;
    Direction direction;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }
}

// OBSERVER PATTERN
interface Observer {
    void update(Elevator e);
}

// ELEVATOR
class Elevator {
    int id;
    int currentFloor = 0;
    Direction direction = Direction.IDLE;

    PriorityQueue<Integer> upQueue = new PriorityQueue<>();
    PriorityQueue<Integer> downQueue = new PriorityQueue<>(Collections.reverseOrder());

    List<Observer> observers = new ArrayList<>();

    void addObserver(Observer o) {
        observers.add(o);
    }

    void notifyObservers() {
        for (Observer o : observers) o.update(this);
    }

    void addRequest(int floor) {
        if (floor > currentFloor) upQueue.offer(floor);
        else downQueue.offer(floor);
    }

    void move() {
        if (!upQueue.isEmpty()) {
            direction = Direction.UP;
            currentFloor = upQueue.poll();
        } else if (!downQueue.isEmpty()) {
            direction = Direction.DOWN;
            currentFloor = downQueue.poll();
        } else {
            direction = Direction.IDLE;
        }
        notifyObservers();
    }
}

// CONTROLLER (SCAN-like)
class ElevatorController {
    List<Elevator> elevators;

    ElevatorController(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    Elevator assignElevator(Request request) {
        Elevator best = elevators.get(0);
        int minDist = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int dist = Math.abs(e.currentFloor - request.floor);
            if (dist < minDist) {
                minDist = dist;
                best = e;
            }
        }
        best.addRequest(request.floor);
        return best;
    }
}
