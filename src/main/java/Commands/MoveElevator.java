package Commands;

import Objects.Elevator;
import Objects.Group;

public class MoveElevator implements Command{

    private final int elevatorNumber;

    public MoveElevator(int elevatorNumber) {

        this.elevatorNumber = elevatorNumber;
    }

    @Override
    public void execute() {
        Elevator elevator = IoC.IoC.resolve("getElevator", elevatorNumber);
        elevator.move();
    }
}
