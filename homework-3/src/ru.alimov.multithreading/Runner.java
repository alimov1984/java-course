package ru.alimov.multithreading;


import java.time.Instant;

public class Runner {

    public static void main(String[] args) {

        PoolService poolService = PoolService.createPool(5);

        for (int i = 0; i < 10; ++i) {
            int index = i;
            poolService.execute(() -> System.out.printf("%s - Executing task %s %s\n", Thread.currentThread().getName(), index, Instant.now()));
        }

        poolService.shutdown();
        poolService.awaitTermination();
    }


}
