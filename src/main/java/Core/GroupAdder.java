package Core;

import Commands.AddGroupToFloor;
import Commands.Command;
import IoC.IoC;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;

public class GroupAdder implements Runnable{
    BlockingDeque<Command> queue;
    int floorsCount;
    @Override
    public void run() {
        int i = 0;
        while (i < 50){
            int count = (int) (Math.random()*5);
            int floorFrom = getRandomFloor();
            int floorTo = getRandomFloor();
            while (floorTo == floorFrom){
                floorTo = getRandomFloor();
            }
            queue.add(new AddGroupToFloor(count, floorFrom, floorTo));
            i++;
            try {
                sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    int getRandomFloor(){
        return (int) (Math.random()*floorsCount);
    }

    public GroupAdder(BlockingDeque<Command> queue) {
        this.queue = queue;
        this.floorsCount = IoC.resolve("floorsCount");
    }
}
