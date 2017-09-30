package edu.neu.distriSys.testClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Tester class receives the arguments from command line, also display the status.
 */
public class ClientTesterStep4 {

  public static void main(String[] args) throws InterruptedException {
    if (args.length != 4) {
      System.out.printf("Illegal arguments!\n" +
          "Arguments should be: Number of threads, " +
          "Number of iterations, " +
          "IP addess of server, " +
          "Port on server!\n");
    } else {
      for (String str : args) {
        System.out.printf(str + ' ');
      }

      long startMilliSec = System.currentTimeMillis(), endMilliSec;
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

      String url = args[2] + ':' + args[3];
      ThreadsProcessor processor = new ThreadsProcessor(url, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
      System.out.println("\nClient starting ......  Time: " + df.format(new Date(startMilliSec)));
      System.out.println("All threads running ......"  + '\n');
      processor.startThreads();

      endMilliSec = System.currentTimeMillis();
      System.out.println("All threads complete ......  Time:" + df.format(new Date(endMilliSec)));
//      processor.showWallTime();
      processor.calAndShowStats();
      System.out.printf("Test Wall time: %.3f seconds", (float) (endMilliSec - startMilliSec) / 1000);

    }

  }
}
