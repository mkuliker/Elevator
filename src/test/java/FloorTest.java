import Objects.*;
import Core.Init;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloorTest {
    @BeforeAll
    static void beforeAll() {
        Init.initialLoad();
    }

    @Test
    void addGroupsTest(){
        UObject object = new CustomObject();
        object.setProperty("buttonStatus", FloorButtonStatus.OFF);
        object.setProperty("peopleGroups", new ArrayList<Group>());
        Floor floor = new FloorImpl(object);
        floor.addPeopleGroup(new GroupImpl(4,5));
        floor.addPeopleGroup(new GroupImpl(9,6));
        assertEquals(2,floor.getPeopleGroups().size());
    }
    @Test
    void changeButtonStatusTest(){
        UObject object = new CustomObject();
        object.setProperty("buttonStatus", FloorButtonStatus.OFF);
        object.setProperty("peopleGroups", new ArrayList<Group>());
        Floor floor = new FloorImpl(object);
        floor.setButtonStatus(FloorButtonStatus.UP);
        assertEquals(FloorButtonStatus.UP,floor.getButtonStatus());
    }
}
