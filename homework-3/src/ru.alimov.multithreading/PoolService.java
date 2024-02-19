package ru.alimov.multithreading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public final class PoolService {
    private final List<Runnable> runnableTaskList;
    private final List<Thread> threadList;
    private final List<Runnable> observerList;
    private final int capacity;
    private boolean isShutdown;
    private final Object syncTaskList = new Object();

    private PoolService(int capacity) {
        this.capacity = capacity;
        this.runnableTaskList = new LinkedList<>();
        this.threadList = new ArrayList<>(this.capacity);
        this.observerList = new ArrayList<>(this.capacity);
    }

    public static PoolService createPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity value must be one at least");
        }
        PoolService poolService = new PoolService(capacity);
        ProcessTask processTask = poolService.new ProcessTask();
        for (int i = 0; i < capacity; ++i) {
            Thread thread = new Thread(processTask);
            thread.start();
            poolService.threadList.add(thread);
        }
        return poolService;
    }

    public void execute(Runnable task) {
        synchronized (syncTaskList) {
            if (this.isShutdown) {
                throw new IllegalStateException();
            }
            runnableTaskList.add(task);
            syncTaskList.notifyAll();
        }
    }

    public void shutdown() {
        System.out.printf("shutdown() started %s\n", Instant.now());
        synchronized (syncTaskList) {
            for (int i = 0; i < this.capacity; ++i) {
                runnableTaskList.add(new FinishTask());
            }
            this.isShutdown = true;
            syncTaskList.notifyAll();
        }
    }

    public void awaitTermination() {
        System.out.printf("awaitTermination() started %s\n", Instant.now());
        Thread awaitTerminationTread = new Thread(
                () -> {
                    try {
                        for (Thread thread : this.threadList) {
                            thread.join();
                        }
                    } catch (InterruptedException e) {
                        System.out.printf("awaitTermination() InterruptedException %s\n", Instant.now());
                        throw new RuntimeException(e);
                    }
                    synchronized (syncTaskList) {
                        if (runnableTaskList.isEmpty()
                                && threadList.stream().filter(t -> t.isAlive()).count() == 0) {
                            System.out.printf("awaitTermination() all threads are finished %s\n", Instant.now());
                        }
                    }
                });
        awaitTerminationTread.start();
    }

    private class ProcessTask implements Runnable {
        @Override
        public void run() {
            System.out.printf("%s - start\n", Thread.currentThread().getName());
            while (!Thread.currentThread().isInterrupted()) {
                Runnable currentTask = null;
                synchronized (syncTaskList) {
                    currentTask = getRunnableTask();
                    if (currentTask == null) {
                        System.out.printf("%s - waiting %s \n", Thread.currentThread().getName(), Instant.now());
                        try {
                            syncTaskList.wait();
                        } catch (InterruptedException e) {
                            System.out.printf("%s InterruptedException %s\n", Thread.currentThread().getName(), Instant.now());
                            Thread.currentThread().interrupt();
                        }
                        System.out.printf("%s - waking up %s\n", Thread.currentThread().getName(), Instant.now());
                        continue;
                    }
                }
                if (currentTask instanceof FinishTask) {
                    System.out.printf("%s - FinishTask is received %s\n", Thread.currentThread().getName(), Instant.now());
                    break;
                }
                currentTask.run();
            }
            System.out.printf("%s - end %s\n", Thread.currentThread().getName(), Instant.now());
        }

        private Runnable getRunnableTask() {
            Runnable currentTask = null;
            if (runnableTaskList != null && !runnableTaskList.isEmpty()) {
                currentTask = runnableTaskList.remove(0);
            }
            return currentTask;
        }
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isShutdown() {
        return this.isShutdown;
    }
}
