package Commands;

import IoC.IoC;
import Objects.Elevator;
import Objects.ElevatorStatus;
import Core.ControlSystem;

import java.util.Arrays;

public class CalculateAndSetElevatorStatus implements Command{
        private final int eNumber;

    public CalculateAndSetElevatorStatus(int eNumber) {
            this.eNumber = eNumber;
        }

        @Override
        public void execute() {
            Elevator elevator = IoC.resolve("getElevator", eNumber);
            ControlSystem cs = IoC.resolve("getCS");
            ElevatorStatus newStatus = elevator.getStatus();
            //у лифта не заполнены этажи: останавливаемся.
            if (Arrays.stream(elevator.getFloors()).sum() == 0) {
                newStatus = ElevatorStatus.OFF;
            } else {
                //у лифта заполнены этажи: либо едем куда ехали, либо разворачиваемся
                int min = cs.getMinFloorForElevator(elevator);
                int max = cs.getMaxFloorForElevator(elevator);
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
            elevator.setStatus(newStatus);
        }
    }

