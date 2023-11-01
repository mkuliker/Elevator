package Commands;

import IoC.IoC;
import Objects.Elevator;

public class SetTargetFloorToElevator implements Command{
    private final int eNumber;
    private final int fNumber;

    public SetTargetFloorToElevator(int eNumber, int fNumber) {
        this.eNumber = eNumber;
        this.fNumber = fNumber;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.resolve("getElevator", eNumber);
        elevator.setTargetFloor(fNumber, 1);
    }
}
