package Commands;

import IoC.IoC;
import Objects.Elevator;
import Objects.Group;
import Objects.Floor;

import java.util.ArrayList;
import java.util.List;

/**
 * Добавить группы с этажа в лифт, удалить группы с этажа
 */
public class AddGroupsFromFloorToElevator implements Command{
    private final int eNumber;

    public AddGroupsFromFloorToElevator(int eNumber) {
        this.eNumber = eNumber;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.resolve("getElevator", eNumber);
        int currentFloor = elevator.getCurrentFloor();
        List<Group> forDel = new ArrayList<>();
        Floor floor = IoC.resolve("getFloor", currentFloor);
        if (floor.getPeopleGroups().size() != 0) {
            for (var g : floor.getPeopleGroups()) {
                processAddingGroupToElevator(elevator, g);
                forDel.add(g);
            }
            forDel.forEach(group -> floor.getPeopleGroups().remove(group));
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
}
