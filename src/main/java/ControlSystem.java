import Commands.Command;
import IoC.IoC;
import Objects.*;

import java.util.*;

public class ControlSystem {
    int tick;
    Elevator[] elevators;
    Floor[] floors;
    List<List<Command>> queue;
    int floorsCount;
    int elevatorsCount;

    Map<Group, Integer> groupToElevator;

    public ControlSystem() {
        this.floorsCount = IoC.resolve("floorsCount");
        this.elevatorsCount = IoC.resolve("elevatorsCount");
        this.floors = new Floor[this.floorsCount];
        this.elevators = new Elevator[this.elevatorsCount];
        this.queue = new LinkedList<>();
        for (int i = 0; i < this.floorsCount; i++) {
            this.floors[i] = IoC.resolve("floor");
        }
        for (int i = 0; i < this.elevatorsCount; i++) {
            this.elevators[i] = IoC.resolve("elevator");
        }
        tick = 0;
        groupToElevator = new HashMap<>();
    }

    /*
        //для каждого этажа с нажатой кнопкой - найти лифт и добавить этаж в список
        //для каждого лифта в движении - сделать один мув
        //для каждого приехавшего лифта - обновить состояние
     */
    void process() {
        printFloors();
        processFloors();
        processElevators();
        tick++;

    }

    private void processElevators() {
        //для каждого приехавшего лифта - обновить состояние
        for (int i = 0; i < this.elevatorsCount; i++) {
            Elevator elevator = elevators[i];
            int currentFloor = elevator.getCurrentFloor();
            if (elevator.getFloors()[currentFloor] == 1) {
                releaseGroupsFromElevator(elevator);
                addGroupsFromFloorToElevator(elevator);
            }
            elevator.setStatus(calcNewStatus(elevator));
        }
        //для каждого лифта в движении - сделать один мув
        for (int i = 0; i < this.elevatorsCount; i++) {
            elevators[i].move();
        }
    }

    private int findElevatorForFloor(int f) {
        for (int i = 0; i < this.elevatorsCount; i++) {
            if (elevators[i].getStatus() == ElevatorStatus.OFF) {
                return i;
            } else if (elevators[i].getStatus() == ElevatorStatus.UP && elevators[i].getCurrentFloor() < f) {
                return i;
            } else if (elevators[i].getStatus() == ElevatorStatus.DOWN && elevators[i].getCurrentFloor() > f) {
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
                    elevators[elevatorNum].setTargetFloor(i, 1);
                    floors[i].setButtonStatus(FloorButtonStatus.OFF);
                }
            }
        }
    }

    /**
     * Добавить группы с этажа в лифт: переместить группу, включить-выключить этажи в лифте
     *
     * @param elevator
     * @param group
     */
    private void processAddingGroupToElevator(Elevator elevator, Group group) {
        elevator.setTargetFloor(group.getTargetFloor(), 1);
        elevator.setTargetFloor(elevator.getCurrentFloor(), 0);
        elevator.addPeopleGroup(group);
    }

    /**
     * Добавить группы с этажа в лифт, удалить группы с этажа
     *
     * @param elevator
     */
    private void addGroupsFromFloorToElevator(Elevator elevator) {
        int currentFloor = elevator.getCurrentFloor();
        List<Group> forDel = new ArrayList<>();
        if (floors[currentFloor].getPeopleGroups().size() != 0) {
            for (var g : floors[currentFloor].getPeopleGroups()) {
                processAddingGroupToElevator(elevator, g);
                forDel.add(g);
            }
            forDel.forEach(group -> floors[currentFloor].getPeopleGroups().remove(group));
        }
    }

    /**
     * Высвободить все группы с g.targerFloor = e.currentFloor, выключить этаж в лифте
     *
     * @param elevator
     */
    private void releaseGroupsFromElevator(Elevator elevator) {
        List<Group> forDel = new ArrayList<>();
        for (var g : elevator.getPeopleGroups()) {
            if (elevator.getCurrentFloor() == g.getTargetFloor()) {
                forDel.add(g);
            }
        }
        elevator.setTargetFloor(elevator.getCurrentFloor(), 0);
        forDel.forEach(group -> elevator.getPeopleGroups().remove(group));
    }

    private ElevatorStatus calcNewStatus(Elevator elevator) {
        ElevatorStatus newStatus = elevator.getStatus();
        //у лифта не заполнены этажи: останавливаемся.
        if (Arrays.stream(elevator.getFloors()).sum() == 0) {
            newStatus = ElevatorStatus.OFF;
        } else {
            //у лифта заполнены этажи: либо едем куда ехали, либо разворачиваемся
            int min = getMinFloorForElevator(elevator);
            int max = getMaxFloorForElevator(elevator);
            int current = elevator.getCurrentFloor();
            if (elevator.getStatus() == ElevatorStatus.DOWN && min != -1 && min < current) {//едем куда ехали
                newStatus = elevator.getStatus();
            } else if (elevator.getStatus() == ElevatorStatus.UP && max != -1 && max > current) {//едем куда ехали
                newStatus = elevator.getStatus();
            } else if (min != -1 && min < current) {
                newStatus = ElevatorStatus.DOWN;
            } else if (max != -1 && max > current) {
                newStatus = ElevatorStatus.UP;
            }
        }
        return newStatus;
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
                result.append("  ")
                        .append(elevators[i].getStatus())
                        .append("|")
                        .append(destinationString(elevators[i]));
            } else {
                result.append("    ");
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

    int getMinFloorForElevator(Elevator elevator) {
        for (int i = 0; i < floorsCount; i++) {
            if (elevator.getFloors()[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    int getMaxFloorForElevator(Elevator elevator) {
        for (int i = floorsCount - 1; i >= 0; i--) {
            if (elevator.getFloors()[i] == 1) {
                return i;
            }
        }
        return -1;
    }

}
