import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


public class Car implements Runnable {
    private static int CARS_COUNT;

    private Race race;
    private int speed;
    private CyclicBarrier barrier;
    private CountDownLatch cdl;
    private AtomicInteger winner;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        cdl.countDown();
        int finishPlace = winner.incrementAndGet();
        if (finishPlace == 1) {
            System.out.println(this.name + " победитель!");
        }
    }

    protected Car(Race race, int speed, CyclicBarrier barrier, CountDownLatch cdl, AtomicInteger winner) {
        this.race = race;
        this.speed = speed;
        this.barrier = barrier;
        this.cdl = cdl;
        this.winner = winner;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
}