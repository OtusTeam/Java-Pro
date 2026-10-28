package ru.otus.gc;

/*
-Xms256m
-Xmx256m
-verbose:gc
-XX:+UseG1GC
*/

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings({"java:S1144", "java:S125"})
public class Boxing {

  static void main() throws InterruptedException {
    calcInteger();
    // calcInt();
    log.info("done");
  }

  private static void calcInteger() throws InterruptedException {
    int limit = Integer.MAX_VALUE;
    Integer counter = 0;
    for (int idx = 0; idx < limit; idx++) {

      counter += 1;

      if (idx % 1_000_000 == 0) {
        Thread.sleep(1000);
      }
    }
    log.info("counter_Integer:{}", counter);
  }

  private static void calcInt() throws InterruptedException {
    int limit = Integer.MAX_VALUE;
    int counter = 0;
    for (int idx = 0; idx < limit; idx++) {

      counter += 1;

      if (idx % 1_000_000 == 0) {
        Thread.sleep(1000);
      }
    }
    log.info("counter_int:{}", counter);
  }
}
