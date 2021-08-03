import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final int CARS_COUNT = 4;

    //позволяет изменять одну переменную несколькими потоками. Честно подсмотренно)0
    public static final AtomicInteger winner = new AtomicInteger(0);

    public static void main(String[] args) {
        theRace();

    }

    public static void theRace() {
        ExecutorService executor = Executors.newFixedThreadPool(CARS_COUNT);
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        CyclicBarrier barrier = new CyclicBarrier(CARS_COUNT + 1,
                ()-> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!"));
        CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), barrier, cdl, winner);
        }

        for (int i = 0; i < cars.length; i++) {
            executor.execute(cars[i]);
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }

        try {
            cdl.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
            executor.shutdown();
        }

    }

}