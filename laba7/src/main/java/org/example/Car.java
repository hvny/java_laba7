package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable{
    private static int CARS_COUNT;
    private static int winnersCount = 0;
    private volatile static boolean winnerFound;
    // private static boolean winnerFound;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    private CyclicBarrier cb;  //синхронизатор
    private CountDownLatch cdl;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    public Car (Race race, int speed, CyclicBarrier cb) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb = cb;
    }
    @Override
    public synchronized void run () {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));

            System.out.println(this.name + " готов");
            cb.await();             //синхронизация всех потоков в одной точке

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            checkWinner(this);
            cb.await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void checkWinner(Car c) {
        if (!winnerFound) {
            winnersCount++;
            System.out.println(c.name + " - ПОБЕДИТЕЛЬ");
            if (winnersCount == 3) {
                winnerFound = true;
            }

        }
//        System.out.println(CARS_COUNT + " COUNTTTTT");
//
//        if (!winnerFound) {
//            System.out.println(c.name + " - ПОБЕДИТЕЛЬ");
//            if (CARS_COUNT <= 3) {
//                System.out.println(winnersCount + "OTHER COUNTTTTT");
//                winnerFound = true;
//            }
//            }
      }
    }



