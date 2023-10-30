import Objects.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    @BeforeAll
    static void beforeAll() {
        Init.initialLoad();
    }
    @Test
    public void addGroupTest(){
        UObject object = new CustomObject();
        object.setProperty("capacity",5);
        object.setProperty("status", ElevatorStatus.OFF);
        object.setProperty("currentOccupancy",0);
        object.setProperty("currentFloor",0);
        object.setProperty("floors",new int[6]);
        object.setProperty("peopleGroups", new ArrayList<Group>());
        Elevator elevator = new ElevatorImpl(object);
        elevator.addPeopleGroup(new GroupImpl(4,5));
        assertEquals(1, elevator.getPeopleGroups().size());
    }
    @Test
    public void addingTooBigGroupCausesError(){
        UObject object = new CustomObject();
        object.setProperty("capacity",1);
        object.setProperty("status", ElevatorStatus.OFF);
        object.setProperty("currentOccupancy",0);
        object.setProperty("currentFloor",0);
        object.setProperty("floors",new int[6]);
        object.setProperty("peopleGroups", new ArrayList<Group>());
        Elevator elevator = new ElevatorImpl(object);
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () ->elevator.addPeopleGroup(new GroupImpl(4,5)),
                "Expected addPeopleGroup to throw"
        );
        assertTrue(thrown.getMessage().contains("no space"));

    }
}