package cn.link.example;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-5-7
 * Time: AM11:05
 * To change this template use File | Settings | File Templates.
 */
class TaskProtion implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;

    TaskProtion(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        //ToDo
        try {
            dowork();
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void dowork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        System.out.println(this + "completed");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d", id);
    }
}

class WaitingTask implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch latch;

    WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        //ToDo
        try {
            latch.await();
            System.out.println("Latch barrier passed for " + this);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }
}

public class CountDownLatchDemo {
    static final int SIZE = 100;

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(SIZE);

        for (int i = 0; i < 10; i++) {
            exec.execute(new WaitingTask(latch));
        }
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new TaskProtion(latch));
        }

        System.out.println("Launched all tasks ");
        exec.shutdown();

    }
}
