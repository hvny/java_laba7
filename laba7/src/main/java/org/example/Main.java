package org.example;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final int CARS_COUNT = 6;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        CyclicBarrier startCb = new CyclicBarrier(CARS_COUNT + 1);
        CyclicBarrier finishCb = new CyclicBarrier(CARS_COUNT);

        CountDownLatch cdl = new CountDownLatch(3);

        AtomicInteger finishedCars = new AtomicInteger(0);

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 100 + (int)(Math.random() * 10), startCb, finishCb, cdl, finishedCars);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }

        try {
            startCb.await();  // Ожидаем старта всех участников
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

            /*cdl.await(); // Ожидаем завершения всех участников
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
