import Commands.AddGroupToFloor;
import Commands.Command;
import IoC.InitCommand;
import IoC.IoC;
import Objects.*;
import Core.ControlSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddGroupTest {
    @BeforeAll
    static void init(){
        new InitCommand().execute();
        IoC.<Command>resolve("IoC.Register","floorsCount",(Function<Object[], Object>) ((args) -> 5)).execute();
        IoC.<Command>resolve("IoC.Register","elevatorsCount",(Function<Object[], Object>) ((args) -> 5)).execute();
        IoC.<Command>resolve("IoC.Register","elevator",(Function<Object[], Object>) ((args) -> {
            UObject object = new CustomObject();
            object.setProperty("capacity",5);
            object.setProperty("status",0);
            object.setProperty("currentFloor",0);
            object.setProperty("peopleGroups", new ArrayList<Group>());////////////////
            return new ElevatorImpl(object);
        })).execute();
        IoC.<Command>resolve("IoC.Register","floor",(Function<Object[], Object>) ((args) -> {
            UObject object = new CustomObject();
            object.setProperty("buttonStatus",0);
            object.setProperty("peopleGroups", new ArrayList<Group>());////////////////
            return new FloorImpl(object);
        })).execute();

        ControlSystem cs = new ControlSystem();
        IoC.<Command>resolve("IoC.Register","getFloor",(Function<Object[], Object>) ((args) -> cs.floors[(int)args[0]])).execute();
        IoC.<Command>resolve("IoC.Register","getElevator",(Function<Object[], Object>) ((args) -> cs.elevators[(int)args[0]])).execute();

    }
    @Test
    void addGroupToFloorTest(){
        AddGroupToFloor cmd = new AddGroupToFloor(2,3,4);
        cmd.execute();
        Floor floor = IoC.resolve("getFloor", 3);
        assertEquals(1,floor.getPeopleGroups().size());
    }
}
