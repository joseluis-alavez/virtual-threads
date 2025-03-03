package com.example.demo.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

@SuppressWarnings({ "unused", "preview" })
public class HttpPlayer {

  private static final int NUM_USERS = 13;

  public static void main(String[] args) {
    // System.out.println("Hello, World!");

    ThreadFactory factory = Thread.ofVirtual().name("request-handler-", 0).factory();

    try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
      IntStream.range(0, NUM_USERS).forEach(i -> executor.submit(new UserRequestHandler()));
    }
  }
}