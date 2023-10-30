package Commands;

import Objects.Elevator;
import Objects.Group;

public class AddGroupToElevator implements Command{

    private final int elevatorNumber;
    private final Group group;

    public AddGroupToElevator(int elevatorNumber, Group group) {

        this.elevatorNumber = elevatorNumber;
        this.group = group;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.IoC.resolve("getElevator", elevatorNumber);
        elevator.addPeopleGroup(group);

    }
}
