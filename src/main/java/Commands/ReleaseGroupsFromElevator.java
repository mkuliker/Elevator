package Commands;

import IoC.IoC;
import Objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Высвободить все группы с g.targerFloor = e.currentFloor, выключить этаж в лифте
 */
public class ReleaseGroupsFromElevator implements Command{
    private final int eNumber;

    public ReleaseGroupsFromElevator(int eNumber) {
        this.eNumber = eNumber;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.resolve("getElevator", eNumber);
        List<Group> forDel = new ArrayList<>();
        for (var g : elevator.getPeopleGroups()) {
            if (elevator.getCurrentFloor() == g.getTargetFloor()) {
                forDel.add(g);
            }
        }
        elevator.setTargetFloor(elevator.getCurrentFloor(), 0);
        forDel.forEach(group -> elevator.getPeopleGroups().remove(group));
    }
}
