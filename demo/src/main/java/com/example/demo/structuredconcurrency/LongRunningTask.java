package com.example.demo.structuredconcurrency;

import java.time.Duration;
import java.util.concurrent.Callable;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class LongRunningTask implements Callable<TaskResponse> {

  private final String name;
  private final int time;
  private final String output;
  private final boolean fail;

  @Override
  public TaskResponse call() throws Exception {
    int numSecs = 0;
    long startTime = System.currentTimeMillis();
    log.info("{} started", this.name);

    while (numSecs++ < this.time) {
      if (Thread.interrupted()) {
        log.info("{} interrupted", this.name);
        throw new InterruptedException("Task interrupted");
      }
      log.info("{} running for {} seconds", this.name, numSecs);
      try {
        Thread.sleep(Duration.ofSeconds(1));
      } catch (InterruptedException e) {
        log.info("{} interrupted", this.name);
        throw new InterruptedException("Task interrupted");
      }
    }

    if (fail) {
      log.error("{} failed", this.name);
      throw new RuntimeException("Task failed");
    }

    log.info("{} done", this.name);

    return new TaskResponse(this.name, this.output, System.currentTimeMillis() - startTime);
  }

}
