package com.example.demo.structuredconcurrency;

import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StructuredTaskScopePlayer {

  public static void main(String[] args) throws InterruptedException, TimeoutException {
    UsesCases usesCases = new UsesCases();
    log.info("Starting main task");
    usesCases.completeAllTasks();
    log.info("Ending main task");
  }

}