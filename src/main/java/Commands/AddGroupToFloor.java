package Commands;

import Objects.Floor;
import IoC.IoC;
import Objects.FloorButtonStatus;
import Objects.GroupImpl;

public class AddGroupToFloor implements Command{
    private final int count;
    private final int currentFloor;
    private final int targetFloor;

    public AddGroupToFloor(int count, int currentFloor, int targetFloor) {
        this.count = count;
        this.currentFloor = currentFloor;
        this.targetFloor = targetFloor;
    }

    @Override
    public void execute() {
        Floor floor = IoC.resolve("getFloor", currentFloor);
        floor.addPeopleGroup(new GroupImpl(count,targetFloor));
        FloorButtonStatus status = currentFloor > targetFloor ? FloorButtonStatus.DOWN : FloorButtonStatus.UP;
        floor.setButtonStatus(status);
    }
}
