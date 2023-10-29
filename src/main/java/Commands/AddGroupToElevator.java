package Commands;

import Objects.Elevator;
import Objects.Group;

public class AddGroupToElevator implements Command{

    private final int elevator;
    private final Group group;

    public AddGroupToElevator(int elevator, Group group) {

        this.elevator = elevator;
        this.group = group;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.IoC.resolve("getElevator", this.elevator);
        elevator.changeOccupancy(this.group.getCount());

    }
}
