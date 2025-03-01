package com.example.demo.services;

import java.io.IOException;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRequestHandler implements Callable<String> {

  @Override
  public String call() throws Exception {

    return getResultSequential();
  }

  private String getResultSequential() throws IOException {
    // Sequential calls
    String result1 = dbCall();
    String result2 = restCall();

    String result = String.format("[%s, %s", result1, result2);
    log.info("{} : {}", Thread.currentThread(), result);

    return result;
  }

  private String dbCall() throws IOException {
    NetworkCaller db = new NetworkCaller("data");
    return db.makeCall(2);
  }

  private String restCall() throws IOException {
    NetworkCaller rest = new NetworkCaller("rest");
    return rest.makeCall(5);
  }

}