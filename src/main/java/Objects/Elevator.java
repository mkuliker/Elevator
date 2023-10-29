package Objects;

import java.util.List;

public interface Elevator {
    int getCurrentFloor();
    int[] getFloors();
    ElevatorStatus getStatus();
    void setStatus(ElevatorStatus status);
    int getCapacity();
    int getCurrentOccupancy();
    void changeOccupancy(int count);
    void setTargetFloor(int floor, int status);
    void move();
    List<Group> getPeopleGroups();
    void addPeopleGroup(Group g);
}
