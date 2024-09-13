package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    private AtomicInteger finishedCars;
    private CyclicBarrier startCb;
    private CyclicBarrier finishCb;

    private CountDownLatch cdl;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier startCb, CyclicBarrier finishCb, CountDownLatch cdl, AtomicInteger finishedCars) {
        this.race = race;
        this.speed = speed;
        this.startCb = startCb;
        this.finishCb = finishCb;
        this.cdl = cdl;
        this.finishedCars = finishedCars;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            startCb.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

            announceFinish();
           cdl.countDown();
            finishCb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void announceFinish() {
        int currentFinished = finishedCars.incrementAndGet();
        if (currentFinished <= 3) {
            System.out.println(this.name + " - " + currentFinished + " место");
        }
        cdl.countDown();
    }
}
