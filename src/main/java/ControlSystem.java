import Commands.Command;
import IoC.IoC;
import Objects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ControlSystem {
    int tick;
    Elevator[] elevators;
    Floor[] floors;
    List<List<Command>> queue;
    int floorsCount;
    int elevatorsCount;

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
    }

    /*
        //для каждого этажа с нажатой кнопкой - найти лифт и добавить этаж в список
        //для каждого лифта в движении - сделать один мув
        //для каждого приехавшего лифта - обновить состояние
     */
    void process() {
        printFloors();
        //для каждого этажа с нажатой кнопкой - найти лифт и добавить этаж в список
        for (int i = 0; i < this.floorsCount; i++) {
            if (floors[i].getButtonStatus() == FloorButtonStatus.ON) {
                elevators[0].setTargetFloor(i, 1);
                floors[i].setButtonStatus(FloorButtonStatus.OFF);
            }
        }
        //для каждого приехавшего лифта - обновить состояние
        for (int i = 0; i < this.elevatorsCount; i++) {
            Elevator elevator = elevators[i];
            int currentFloor = elevator.getCurrentFloor();
            if (elevator.getFloors()[currentFloor] == 1) {
                //группа из лифта
                List<Group> forDel = new ArrayList<>();
                for (var g : elevator.getPeopleGroups()) {
                    if (currentFloor == g.getTargetFloor()) {

                        elevator.changeOccupancy(-1 * g.getCount());
                        forDel.add(g);
                        // обновить состояние, если лифт приехал
                        if (elevator.getCurrentOccupancy() == 0) {
                            elevator.setStatus(ElevatorStatus.OFF);
                        }
                    }
                }
                forDel.forEach(group -> elevator.getPeopleGroups().remove(group));
                //группа в лифт
                if (floors[currentFloor].getPeopleGroups().size() != 0) {
                    forDel = new ArrayList<>();
                    for (var g : floors[currentFloor].getPeopleGroups()) {
                        elevator.changeOccupancy(g.getCount());
                        elevator.setTargetFloor(g.getTargetFloor(), 1);
                        elevator.setTargetFloor(currentFloor, 0);
                        elevator.setStatus(calcNewStatus(elevator));
                        elevator.addPeopleGroup(g);
                        forDel.add(g);
                    }
                    forDel.forEach(group -> floors[currentFloor].getPeopleGroups().remove(group));
                }

            }
        }
        //для каждого лифта в движении - сделать один мув
        for (int i = 0; i < this.elevatorsCount; i++) {
                elevators[i].move();
        }
        tick++;

    }

    private ElevatorStatus calcNewStatus(Elevator elevator) {
        ElevatorStatus newStatus = elevator.getStatus();
        if (newStatus == ElevatorStatus.OFF) {
            for (int i = 0; i < floorsCount; i++) {
                if (elevator.getFloors()[i] == 1 && i < elevator.getCurrentFloor()) {
                    newStatus = ElevatorStatus.DOWN;
                }
                if (elevator.getFloors()[i] == 1 && i > elevator.getCurrentFloor()) {
                    newStatus = ElevatorStatus.UP;
                }
            }
        }
        else if (newStatus == ElevatorStatus.DOWN && elevator.getCurrentFloor() == 0) {
            newStatus = ElevatorStatus.OFF;
        }
        else if (newStatus == ElevatorStatus.UP && elevator.getCurrentFloor() == floorsCount - 1) {
            newStatus = ElevatorStatus.OFF;
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
                result.append("  " + elevators[i].getStatus());
                result.append("|");
                result.append(elevators[i].getCurrentOccupancy());
            } else{
                result.append("    ");
            }
        }
        return result.toString();
    }

}
