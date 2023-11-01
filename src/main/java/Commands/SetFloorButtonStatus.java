package Commands;

import IoC.IoC;
import Objects.Floor;
import Objects.FloorButtonStatus;

public class SetFloorButtonStatus implements Command{
    private final int floorNum;
    private final FloorButtonStatus status;

    public SetFloorButtonStatus(int floorNum, FloorButtonStatus status) {
        this.floorNum = floorNum;
        this.status = status;
    }

    @Override
    public void execute() {
        Floor floor = IoC.resolve("getFloor", floorNum);
        floor.setButtonStatus(status);
    }
}
