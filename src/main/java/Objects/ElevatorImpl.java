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

    @SuppressWarnings("unchecked")
    @Override
    public int getCurrentOccupancy() {
        List<Group> groups = (List<Group>) object.getProperty("peopleGroups");
        return groups.stream().map(Group::getCount).reduce(Integer::sum).orElse(0);
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
    @SuppressWarnings("unchecked")
    public List<Group> getPeopleGroups(){
        return (List<Group>) object.getProperty("peopleGroups");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addPeopleGroup(Group g) {
        int currentOccupancy = getCurrentOccupancy();
        int newOccupancy = currentOccupancy + g.getCount();
        if (newOccupancy >= 0 && newOccupancy <= getCapacity()){
            List<Group> groups = (List<Group>) object.getProperty("peopleGroups");
            groups.add(g);
        } else{
            throw new RuntimeException("no space");
        }


    }
}
