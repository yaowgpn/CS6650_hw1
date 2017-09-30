package edu.neu.distriSys.testClient;

;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Each thread that the threadProcessor starts, repetitively sent request to the HTTP endpoints by the time of
 * given iteration(100X) and stores the latency of each request.
 * Created by wangy on 9/28/2017.
 */
public class ClientThread extends Thread {
  private String url;
  private int iterNum;
  private int requestsSentCount = 0;
  private int succResposesCount = 0;
  private List<Long> latencies;
  private CyclicBarrier barrier;

  /**
   * The constructor for a client's thread
   * @param url the url for the server.
   * @param iterNum the times of iteration.
   * @
   * latencies: the elapsed time for each request, in nanoseconds.
   */
  public ClientThread(String url, int iterNum, CyclicBarrier barrier) {
    this.url = url;
    this.iterNum = iterNum;
    this.latencies = new ArrayList<>(2 * iterNum);
    this.barrier = barrier;
  }

  public int getRequestsSentCount() {
    return requestsSentCount;
  }

  public int getSuccResposesCount() {
    return succResposesCount;
  }

  public List<Long> getLatencies() {
    return latencies;
  }

  /**
   * let the HTTP client send a GET request, and also check if the request succeeds.
   * @param client the HTTP client to send the request
   * @return the status of the GET response, 200 indicates success.
   */
  public int doGet(SimpleClient client) {
    Response getResponse = client.sendGet();
    requestsSentCount++;
//    System.out.println(getResponse);
    int status = getResponse.getStatus();
    getResponse.close();
    return status;
  }

  /**
   * let the HTTP client send a POST request, and also check if the request succeeds.
   * @param client the HTTP client to send the request
   * @return the status of the GET response, 200 indicates success.
   */
  public int doPost(SimpleClient client) {
    Response postResponse = client.sendPost("Hello, post", Response.class);
    requestsSentCount++;
//    System.out.println(postResponse);
    int status = postResponse.getStatus();
    postResponse.close();
    return status;
  }

  @Override
  public void run() {
    SimpleClient simpleClient = new SimpleClient(url);
    long startTime, finishTime;
    //To call HTTP endpoints iterNum of times;
    for (int i  = 0; i < iterNum; i++) {
      startTime = System.nanoTime();
      int status = doGet(simpleClient);
      finishTime = System.nanoTime();
      if (status == 200) {
        latencies.add(finishTime - startTime);
      }

      startTime = System.nanoTime();
      status = doPost(simpleClient);
      finishTime = System.nanoTime();
      if (status == 200) {
        latencies.add(finishTime - startTime);
      }
    }
    simpleClient.closeClient();
    succResposesCount = latencies.size();
    try {
      barrier.await();
    } catch (InterruptedException exp) {
      System.out.println("Interrupted exception: " + exp.getMessage());
    } catch (BrokenBarrierException exp) {
      System.out.println("BrokenBarrier exception: " + exp.getMessage());
    }
  }

}
