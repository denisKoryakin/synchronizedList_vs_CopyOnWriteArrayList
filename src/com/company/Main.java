package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /** Приложение сравнения быстродействия synchronizedList и CopyOnWriteArrayList при чтении и редкой записи.
         * Для активации записи требуется раскоментировать строки в классе ListRunner и Main */

        List<Integer> list1 = Collections.synchronizedList(new ArrayList<Integer>());

        List<Integer> list2 = new CopyOnWriteArrayList<>();

        // заполняем листы
        fillList(list1, 100);
        fillList(list2, 100);

        System.out.println("SynchronizedList: ");
        System.out.println("чтение: ");
        checkList(list1, false);

//        System.out.println("чтение и запись: ");
//        checkList(list1, true);

        System.out.println("CopyOnWriteArrayList: ");
        System.out.println("чтение: ");
        checkList(list2, false);

//        System.out.println("чтение и запись: ");
//        checkList(list1, true);
    }

    private static void checkList(List<Integer> list, boolean write) throws ExecutionException, InterruptedException {
        /* Синхронизируем потоки для одновременного запуска с помощью CountDownLatch */
        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService ex = Executors.newFixedThreadPool(4);
        Future<Long> f1 = ex.submit(new ListRunner(0, 25, list, latch, write));
        Future<Long> f2 = ex.submit(new ListRunner(26, 50, list, latch, write));
        Future<Long> f3 = ex.submit(new ListRunner(51, 75, list, latch, write));
        Future<Long> f4 = ex.submit(new ListRunner(76, 100, list, latch, write));

        /* Старт для потоков! */
        latch.countDown();

        System.out.println("Thread1: " + f1.get()/1000 + " мкс");
        System.out.println("Thread2: " + f2.get()/1000 + " мкс");
        System.out.println("Thread3: " + f3.get()/1000 + " мкс");
        System.out.println("Thread4: " + f4.get()/1000 + " мкс");

        ex.shutdown();
    }

    private static void fillList(List<Integer> list, int num) {
        for(int i = 0; i < num; i++) {
            list.add(i);
        }
    }
}
