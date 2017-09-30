package edu.neu.distriSys.testClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class that starts the threads and calculates the statics of latencies of the threads.
 */
public class ThreadsProcessor {
  private final String url;
  private final int threadNum;
  private final int iterationNum;
  private List<ClientThread> clientThreads;
  private int requestCount = 0;
  private int succRespCount = 0;
  private CyclicBarrier barrier;

  //array contains the mean, media, 95-percentile and 99-percentile of all the latencies
  private long[] latencyStats;

  /**
   * Constructor for ThreadsProcessor.
   * @param threadNum the number of threads.
   * @param iterationNum times of iteration for each thread.
   */
  public ThreadsProcessor(String url, int threadNum, int iterationNum) {
    if (threadNum <= 0 || iterationNum <= 0) {
      throw new IllegalArgumentException("thread number and iteration number must > 0");
    }
    this.url = url;
    this.threadNum = threadNum;
    this.iterationNum = iterationNum;
    this.clientThreads = new ArrayList<>(threadNum);
    this.barrier = new CyclicBarrier(threadNum);
    latencyStats = new long[4];
  }

  public void startThreads() throws InterruptedException {
    ExecutorService threadpool = Executors.newFixedThreadPool(threadNum);
    for (int i = 0; i < threadNum; i++) {
      ClientThread thread = new ClientThread(url, iterationNum, barrier);
      clientThreads.add(thread);
      threadpool.execute(thread);
    }
    threadpool.shutdown();
    threadpool.awaitTermination(1, TimeUnit.DAYS);
  }

  public void counter() {
    for (ClientThread clientThread : clientThreads) {
      requestCount += clientThread.getRequestsSentCount();
      succRespCount += clientThread.getSuccResposesCount();
    }
  }

  public void calcStats() {
    long[] allLatencies = new long[succRespCount];
    int count = 0;
    long latSum = 0;
    for (ClientThread thread : clientThreads) {
      for (long latency : thread.getLatencies()) {
        allLatencies[count++] = latency;
        latSum += latency;
      }
    }
    latencyStats[0] = latSum / count;
    Arrays.sort(allLatencies);
    latencyStats[1] = allLatencies[succRespCount / 2 - 1];
    latencyStats[2] = allLatencies[succRespCount * 95 / 100 - 1];
    latencyStats[3] = allLatencies[succRespCount * 99 / 100 - 1];
  }

  public void showWallTime() {
    counter();
    System.out.println("Statics of threads:\n");
    System.out.println("============================================================");
    System.out.println("Total number of requests sent: " + requestCount);
    System.out.println("Total number of successful responses: " + succRespCount);
  }

  public void calAndShowStats() {
    showWallTime();
    calcStats();
    System.out.println("============================================================");
    System.out.println("The mean of all latencies: " + latencyStats[0] + " nanaosecs.");
    System.out.println("The median of all latencies: " + latencyStats[1] + " nanaosecs.");
    System.out.println("The 95th percentile of all latencies: " + latencyStats[2] + " nanaosecs.");
    System.out.println("The 99th percentile of all latencies: " + latencyStats[3] + " nanaosecs.");

  }

}
