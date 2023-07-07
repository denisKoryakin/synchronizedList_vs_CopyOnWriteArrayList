package com.company;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ListRunner implements Callable<Long> {

    int start;
    int end;
    List<Integer> list;
    CountDownLatch latch;
    boolean write;

    public ListRunner(int start, int end, List<Integer> list, CountDownLatch latch, boolean write) {
        this.start = start;
        this.end = end;
        this.list = list;
        this.latch = latch;
        this.write = write;
    }

    @Override
    public Long call() throws Exception {
        latch.await();

        long startTime = System.nanoTime();
        for (int i = start; i < end; i++) {
//            if (write = true) {
//                // заменяем элемент на каждой 2 итерации
//                if (i % 2 == 0) {
//                    list.add(i, i);
//                }
//            }
            list.get(i);
        }
        return System.nanoTime() - startTime;
    }
}
