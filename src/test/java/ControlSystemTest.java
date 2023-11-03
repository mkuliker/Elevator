import Commands.AddGroupToFloor;
import Commands.Command;
import IoC.IoC;
import Objects.ElevatorStatus;
import Core.ControlSystem;
import Core.Init;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ControlSystemTest {

    @BeforeAll
    static void beforeAll() {
        Init.initialLoad();
        IoC.<Command>resolve("IoC.Register","debugMode",(Function<Object[], Object>) ((args) -> true)).execute();

    }
    @Test
    public void mainTest(){
        ControlSystem cs = IoC.resolve("getCS");
        Deque<Command> queue = IoC.resolve("getQueue");

        queue.add(new AddGroupToFloor(1,3,4));

        while (cs.tick < 17){
            if (cs.tick == 2){
                queue.add(new AddGroupToFloor(1,0,4));
            }
            cs.process();
            if ((cs.tick >= 2 && cs.tick <= 4)
            ||(cs.tick >= 11 && cs.tick <= 13)){
                assertEquals(ElevatorStatus.UP, cs.elevators[0].getStatus());
            }
            if (cs.tick == 15){
                assertEquals(ElevatorStatus.OFF, cs.elevators[0].getStatus());
            }
        }
    }
    public void whenFloorButtonIsOnElevatorHasFloorInListTest(){
        //changeFloorStatus
        //assert at least one elevator is going to this floor
    }
}