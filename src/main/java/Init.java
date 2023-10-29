import Commands.Command;
import IoC.InitCommand;
import IoC.IoC;
import Objects.*;

import java.util.ArrayList;
import java.util.function.Function;

public class Init {
    public static void initialLoad() {
        new InitCommand().execute();
        int floorsCount = 5;
        int elevatorsCount = 2;
        IoC.<Command>resolve("IoC.Register","floorsCount",(Function<Object[], Object>) ((args) -> floorsCount)).execute();
        IoC.<Command>resolve("IoC.Register","elevatorsCount",(Function<Object[], Object>) ((args) -> elevatorsCount)).execute();
        IoC.<Command>resolve("IoC.Register","elevator",(Function<Object[], Object>) ((args) -> {
            UObject object = new CustomObject();
            object.setProperty("capacity",5);
            object.setProperty("status", ElevatorStatus.OFF);
            object.setProperty("currentOccupancy",0);
            object.setProperty("currentFloor",0);
            object.setProperty("floors",new int[floorsCount]);
            object.setProperty("peopleGroups", new ArrayList<Group>());////////////////
            return new ElevatorImpl(object);
        })).execute();
        IoC.<Command>resolve("IoC.Register","floor",(Function<Object[], Object>) ((args) -> {
            UObject object = new CustomObject();
            object.setProperty("buttonStatus",FloorButtonStatus.OFF);
            object.setProperty("peopleGroups", new ArrayList<Group>());////////////////
            return new FloorImpl(object);
        })).execute();

        ControlSystem cs = new ControlSystem();
        IoC.<Command>resolve("IoC.Register","getFloor",(Function<Object[], Object>) ((args) -> cs.floors[(int)args[0]])).execute();
        IoC.<Command>resolve("IoC.Register","getCS",(Function<Object[], Object>) ((args) -> cs)).execute();
        IoC.<Command>resolve("IoC.Register","getElevator",(Function<Object[], Object>) ((args) -> cs.elevators[(int)args[0]])).execute();

    }
}
