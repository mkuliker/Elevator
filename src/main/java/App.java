import Commands.AddGroupToFloor;
import Commands.Command;
import IoC.InitCommand;
import IoC.IoC;
import Objects.*;

import java.util.*;
import java.util.function.Function;

public class App {
    static ControlSystem cs;
    public static void main(String[] args) {
        cs = IoC.resolve("getCS");

        Command cmd = new AddGroupToFloor(2,0,1);
        cmd.execute();

        while (cs.tick < 17){
            if (cs.tick == 2){
                cmd = new AddGroupToFloor(2,0,4);
                cmd.execute();
            }
            cs.process();
        }

    }
}
