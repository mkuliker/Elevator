package Core;

import Commands.*;
import IoC.IoC;
import Objects.*;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ControlSystem {
    public int tick;
    public Elevator[] elevators;
    public Floor[] floors;
    BlockingDeque<Command> queue;
    int floorsCount;
    int elevatorsCount;
    Supplier<Boolean> debugMode;

    public ControlSystem() {
        this.floorsCount = IoC.resolve("floorsCount");
        this.elevatorsCount = IoC.resolve("elevatorsCount");
        this.floors = new Floor[this.floorsCount];
        this.elevators = new Elevator[this.elevatorsCount];
        this.queue = new LinkedBlockingDeque<>();
        for (int i = 0; i < this.floorsCount; i++) {
            this.floors[i] = IoC.resolve("floor");
        }
        for (int i = 0; i < this.elevatorsCount; i++) {
            this.elevators[i] = IoC.resolve("elevator");
        }
        tick = 0;
        debugMode = IoC.resolve("debugModeChecker");
    }

    /*
        //для каждого этажа с нажатой кнопкой - найти лифт и добавить этаж в список
        //для каждого лифта в движении - сделать один мув
        //для каждого приехавшего лифта - обновить состояние
     */
    public void process() {
        if (debugMode.get()) {
            printFloors();
        }
        processQueue();
        processFloors();
        processElevators();
        tick++;
    }

    private void processQueue() {
        while (!queue.isEmpty()) {
            Command cmd = queue.remove();
            cmd.execute();
        }
    }

    private void processElevators() {
        for (int i = 0; i < this.elevatorsCount; i++) {
            if (needProcessElevator(i)) {
                queue.add(new ReleaseGroupsFromElevator(i));
                queue.add(new AddGroupsFromFloorToElevator(i));
            }
            queue.add(new CalculateAndSetElevatorStatus(i));
            queue.add(new MoveElevator(i));
        }
    }

    private boolean needProcessElevator(int eNum) {
        Elevator elevator = elevators[eNum];
        int currentFloor = elevator.getCurrentFloor();
        return elevator.getFloors()[currentFloor] == 1;
    }

    private int findElevatorForFloor(int f) {
        Set<FloorButtonStatus> dest = floors[f].getPeopleGroups().stream().map(group -> group.getTargetFloor()> f? FloorButtonStatus.UP : FloorButtonStatus.DOWN).collect(Collectors.toSet());
        for (int i = 0; i < this.elevatorsCount; i++){
            if (elevators[i].getStatus() == ElevatorStatus.OFF && elevators[i].getCurrentFloor() == f) {
                return i;
            }
        }
        for (int i = 0; i < this.elevatorsCount; i++) {
            if (elevators[i].getStatus() == ElevatorStatus.OFF) {
                return i;
            } else if (elevators[i].getStatus() == ElevatorStatus.UP && elevators[i].getCurrentFloor() < f && dest.contains(FloorButtonStatus.UP)) {
                return i;
            } else if (elevators[i].getStatus() == ElevatorStatus.DOWN && elevators[i].getCurrentFloor() > f && dest.contains(FloorButtonStatus.DOWN)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * для каждого этажа с нажатой кнопкой - найти лифт и добавить этаж в список лифта, сбросить кнопку этажа
     */
    private void processFloors() {
        for (int i = 0; i < this.floorsCount; i++) {
            if (floors[i].getButtonStatus() != FloorButtonStatus.OFF) {
                int elevatorNum = findElevatorForFloor(i);
                if (elevatorNum != -1) {
                    queue.add(new SetTargetFloorToElevator(elevatorNum, i));
                    queue.add(new SetFloorButtonStatus(i, FloorButtonStatus.OFF));
                }
            }
        }
    }

    private void printFloors() {
        System.out.println("-- tick " + tick + " ---");
        for (int i = floorsCount - 1; i >= 0; i--) {
            System.out.println("" + i + " "
                    + (floors[i].getButtonStatus())
                    + "|" + floors[i].getPeopleGroups().size()
                    + elevatorInfoForFloor(i)
            );
        }
    }

    private String elevatorInfoForFloor(int currentFloor) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < elevatorsCount; i++) {
            if (currentFloor == elevators[i].getCurrentFloor()) {
                result.append("| ")
                        .append(elevators[i].getStatus())
                        .append("|")
                        .append(destinationString(elevators[i]));
            } else {
                result.append("|    ");
            }
        }
        return result.toString();
    }

    int destination(Elevator elevator) {
        int result = -1;
        if (elevator.getStatus() == ElevatorStatus.UP) {
            result = getMaxFloorForElevator(elevator);
        }
        if (elevator.getStatus() == ElevatorStatus.DOWN) {
            result = getMinFloorForElevator(elevator);
        }
        return result;
    }

    String destinationString(Elevator elevator) {
        Integer destination = destination(elevator);
        return destination == -1 ? "-" : destination.toString();
    }

    public int getMinFloorForElevator(Elevator elevator) {
        for (int i = 0; i < floorsCount; i++) {
            if (elevator.getFloors()[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    public int getMaxFloorForElevator(Elevator elevator) {
        for (int i = floorsCount - 1; i >= 0; i--) {
            if (elevator.getFloors()[i] == 1) {
                return i;
            }
        }
        return -1;
    }
}
