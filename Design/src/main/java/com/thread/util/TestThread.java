package com.thread.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * create by Dale
 * create on 2019/5/24
 * description:
 */
public class TestThread {
    static ExecutorService executorService = Executors.newCachedThreadPool();//无界线程池
    static ExecutorService executorService1 = Executors.newFixedThreadPool(3);//固定大小的线程池
   static ExecutorService executorService2 = Executors.newSingleThreadExecutor();//单一线程池
    static List<Future<String>> resultList = new ArrayList<Future<String>>();
    public static void main(String[] args) {
        for (int count = 0; count < 100; count++) {
            Future<String> future = executorService.submit(new TaskWithResult());
            resultList.add(future);
        }

        for (Future<String> fs : resultList) {
            try {
                System.out.println(fs.get());     //打印各个线程（任务）执行的结果
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用。
                executorService.shutdown();
            }
        }
    }

    static class TaskWithResult implements Callable<String>{

        @Override
        public String call() throws Exception {
            return "----";
        }
    }
}
