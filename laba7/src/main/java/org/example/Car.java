package org.example;

import java.util.concurrent.ConcurrentHashMap;
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
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;
    private AtomicInteger finishedCars;
    private ConcurrentHashMap<String, Integer> finishTimes;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CountDownLatch startLatch, CountDownLatch finishLatch, AtomicInteger finishedCars, ConcurrentHashMap<String, Integer> finishTimes) {
        this.race = race;
        this.speed = speed;
        this.startLatch = startLatch;
        this.finishLatch = finishLatch;
        this.finishedCars = finishedCars;
        this.finishTimes = finishTimes;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            startLatch.countDown();
            startLatch.await();

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

            int finishTime = (int) System.currentTimeMillis();
            finishTimes.put(this.name, finishTime);
            announceFinish();

            finishLatch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private synchronized void announceFinish() {
        int currentFinished = finishedCars.incrementAndGet();
        if (currentFinished <= 3) {
            System.out.println(this.name + " - " + currentFinished + " место");
        }
    }
}


