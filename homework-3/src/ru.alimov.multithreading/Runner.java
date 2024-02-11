package ru.alimov.multithreading;



public class Runner {

    public static void main(String[] args) {

        PoolService poolService = PoolService.createPool(5);

        for (int i = 0; i < 10; ++i) {
            int index = i;
            poolService.execute(() -> System.out.printf("%s - Executing task %s\n", Thread.currentThread().getName(), index));
        }

        poolService.shutdown();
        poolService.awaitTermination();
    }


}
