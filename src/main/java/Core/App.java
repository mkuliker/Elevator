package Core;

import Commands.Command;
import IoC.IoC;

import java.util.concurrent.BlockingDeque;

public class App {
    static ControlSystem cs;
    public static void main(String[] args) {
        Init.initialLoad(10,3,100);
        cs = IoC.resolve("getCS");
        BlockingDeque<Command> queue = IoC.resolve("getQueue");

        Thread thread = new Thread(new GroupAdder(queue));
        thread.start();

        while (cs.tick < 100){
            cs.process();
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
