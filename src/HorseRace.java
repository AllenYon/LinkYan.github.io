package cn.link.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-5-8
 * Time: PM4:27
 * To change this template use File | Settings | File Templates.
 */

class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;

    public Horse(CyclicBarrier b) {
        barrier = b;
    }

    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        //ToDo
        while (!Thread.interrupted()) {
            synchronized (this) {
                strides += rand.nextInt(3);
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (BrokenBarrierException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    @Override
    public String toString() {
        return "Horse " + id + " ";
    }

    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }
}

public class HorseRace {
    static final int FINISH_LINE = 80;
    private List<Horse> horses = new ArrayList<Horse>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;

    public HorseRace(int nHorses, final int pause) {
        barrier = new CyclicBarrier(nHorses, new Runnable() {
            @Override
            public void run() {
                //ToDo
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < FINISH_LINE; i++) {
                    s.append("=");
                }
                System.out.println(s);
                for (Horse horse : horses) {
                    System.out.println(horse.tracks());
                }

                for (Horse horse : horses) {
                    if (horse.getStrides() >= FINISH_LINE) {
                        System.out.println(horse + "won!");
                        exec.shutdownNow();
                        return;
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }
        });

        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 200;

//        if (args.length > 0) {
//            int n = new Integer(args[0]);
//            nHorses = n > 0 ? n : nHorses;
//        }
        new HorseRace(nHorses, pause);

    }
}
