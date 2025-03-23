package com.example.demo.services;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRequestHandler implements Callable<String> {

  @Override
  public String call() throws Exception {

    long start = System.currentTimeMillis();
    String result = getResultCompletableFutureHomeWork();
    long end = System.currentTimeMillis();
    log.info("{} : {}", Thread.currentThread(), end - start);
    return result;
  }

  private String getResultCompletableFutureHomeWork() throws IOException, InterruptedException, ExecutionException {

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      CompletableFuture<String> dbFuture = CompletableFuture.supplyAsync(() -> {
        try {
          return dbCall1();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }, executor)
          .thenApply(
              (db) -> {
                try {
                  return dbCall2();
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              })
          .thenCompose(db -> CompletableFuture.supplyAsync(() -> {
            try {
              return restCall1();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }, executor)
              .thenCombine(
                  CompletableFuture.supplyAsync(() -> {
                    try {
                      return restCall2();
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  }, executor),
                  (rest1, rest2) -> String.format("[%s, %s]", rest1, rest2)));

      return dbFuture.get();
    }
  }

  private String getResultCompletableFuture() throws IOException, InterruptedException, ExecutionException {

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

      CompletableFuture<String> dbFuture = CompletableFuture.supplyAsync(() -> {
        try {
          return dbCall();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }, executor).thenCombine(CompletableFuture.supplyAsync(() -> {
        try {
          return restCall();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }, executor), (db, rest) -> String.format("[%s, %s]", db, rest));

      return dbFuture.get();
    }
  }

  private String getResultParallelFuture() throws IOException, InterruptedException, ExecutionException {

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      Future<String> dbFuture = executor.submit(this::dbCall);
      Future<String> restFuture = executor.submit(this::restCall);
      return String.format("[%s, %s", dbFuture.get(), restFuture.get());
    }
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

  private String dbCall1() throws IOException {
    NetworkCaller db = new NetworkCaller("data1");
    return db.makeCall(2);
  }

  private String dbCall2() throws IOException {
    NetworkCaller db = new NetworkCaller("data2");
    return db.makeCall(3);
  }

  private String restCall1() throws IOException {
    NetworkCaller rest = new NetworkCaller("rest1");
    return rest.makeCall(4);
  }

  private String restCall2() throws IOException {
    NetworkCaller rest = new NetworkCaller("rest2");
    return rest.makeCall(5);
  }

}