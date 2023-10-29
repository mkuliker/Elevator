package Objects;

import java.util.List;

public class ElevatorImpl implements Elevator{
    UObject object;

    public ElevatorImpl(UObject object) {
        this.object = object;
    }

    @Override
    public int getCurrentFloor() {
        return (int) object.getProperty("currentFloor");
    }

    @Override
    public int[] getFloors() {
        return (int[]) object.getProperty("floors");
    }

    @Override
    public ElevatorStatus getStatus() {
        return (ElevatorStatus) object.getProperty("status");
    }

    @Override
    public void setStatus(ElevatorStatus status) {
        object.setProperty("status", status);
    }

    @Override
    public int getCapacity() {
        return (int) object.getProperty("capacity");
    }

    @Override
    public int getCurrentOccupancy() {
        return (int) object.getProperty("currentOccupancy");
    }

    @Override
    public void changeOccupancy(int count) {
        int currentOccupancy = getCurrentOccupancy();
        int newOccupancy = currentOccupancy + count;
        if (newOccupancy >= 0 && newOccupancy <= getCapacity()){
            object.setProperty("currentOccupancy", newOccupancy);
        } else{
            throw new RuntimeException("no space");
        }
    }

    @Override
    public void setTargetFloor(int floor, int status) {
        int[] floors = (int[]) object.getProperty("floors");
        floors[floor] = status;
    }
    @Override
    public void move(){
        if (getStatus() == ElevatorStatus.OFF){
            return;
        }
        int newFloor = getCurrentFloor() + getDirection();
        object.setProperty("currentFloor", newFloor);
    }

    private int getDirection() {
        return getStatus() == ElevatorStatus.UP ? 1 : -1;
    }

    @Override
    public List<Group> getPeopleGroups(){
        return (List<Group>) object.getProperty("peopleGroups");
    }

    @Override
    public void addPeopleGroup(Group g) {
        List<Group> groups = (List<Group>) object.getProperty("peopleGroups");
        groups.add(g);

    }
}
