package ru.alimov.multithreading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public final class PoolService {
    private final List<Runnable> runnableTaskList;
    private final List<AThread> threadList;
    private final int capacity;
    private boolean isShutdown;
    private final Object syncGetTask = new Object();
    private final Object syncAddTask = new Object();

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isShutdown() {
        return this.isShutdown;
    }

    private PoolService(int capacity) {
        this.capacity = capacity;
        this.runnableTaskList = new LinkedList<>();
        this.threadList = new ArrayList<>(this.capacity);
    }

    public static PoolService createPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity value must be one at least");
        }
        PoolService poolService = new PoolService(capacity);
        for (int i = 0; i < capacity; ++i) {
            AThread aThread = poolService.new AThread();
            poolService.threadList.add(aThread);
            aThread.start();
        }
        return poolService;
    }

    public void execute(Runnable task) {
        synchronized (syncAddTask) {
            if (this.isShutdown) {
                throw new IllegalStateException();
            }
            runnableTaskList.add(task);
        }
    }

    public void shutdown() {
        synchronized (syncAddTask) {
            for (int i = 0; i < this.capacity; ++i) {
                runnableTaskList.add(new FinishTask());
            }
            this.isShutdown = true;
        }
    }

    private Runnable getRunnableTask() {
        Runnable currentTask = null;
        synchronized (syncGetTask) {
            if (this.runnableTaskList != null && !this.runnableTaskList.isEmpty()) {
                currentTask = this.runnableTaskList.get(0);
                this.runnableTaskList.remove(0);
            }
        }
        return currentTask;
    }

    public void awaitTermination() {
        System.out.printf("awaitTermination() started %s\n", Instant.now());
        while (!Thread.currentThread().isInterrupted()) {
            if (runnableTaskList.isEmpty()
                    && threadList.stream().filter(t -> t.isAlive()).count() == 0) {
                System.out.printf("awaitTermination() executed %s\n", Instant.now());
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class AThread extends Thread {
        @Override
        public void run() {
            System.out.printf("%s - start\n", this.getName());
            while (!this.isInterrupted()) {
                Runnable currentTask = getRunnableTask();
                if (currentTask == null) {
                    continue;
                }
                if (currentTask instanceof FinishTask) {
                    System.out.printf("%s - FinishTask is received\n", this.getName());
                    break;
                }
                currentTask.run();
            }
            System.out.printf("%s - end\n", this.getName());
        }
    }
}
