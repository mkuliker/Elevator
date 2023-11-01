import Commands.AddGroupToFloor;
import Commands.Command;
import IoC.IoC;
import Objects.ElevatorStatus;
import Core.ControlSystem;
import Core.Init;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

        Command cmd = new AddGroupToFloor(1,3,4);
        cmd.execute();

        while (cs.tick < 17){
            if (cs.tick == 2){
                cmd = new AddGroupToFloor(1,0,4);
                cmd.execute();
            }
            cs.process();
            if ((cs.tick >= 1 && cs.tick <= 3)
            ||(cs.tick >= 10 && cs.tick <= 12)){
                assertEquals(ElevatorStatus.UP, cs.elevators[0].getStatus());
            }
            if (cs.tick == 14){
                assertEquals(ElevatorStatus.OFF, cs.elevators[0].getStatus());
            }
        }

    }
    public void whenFloorButtonIsOnElevatorHasFloorInListTest(){
        //changeFloorStatus
        //assert at least one elevator is going to this floor
    }
}