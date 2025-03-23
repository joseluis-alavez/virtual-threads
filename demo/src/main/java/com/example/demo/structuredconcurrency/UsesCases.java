package com.example.demo.structuredconcurrency;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsesCases {

  public void completeAllTasks() throws InterruptedException, TimeoutException {

    try (var scope = new StructuredTaskScope<TaskResponse>()) {

      var expTask = new LongRunningTask("expedia-task", 3, "$100", true);
      var hotTask = new LongRunningTask("hotwire-task", 10, "$110", true);

      // Start running task in parallel
      StructuredTaskScope.Subtask<TaskResponse> expSubTask = scope.fork(expTask);
      StructuredTaskScope.Subtask<TaskResponse> hotSubTask = scope.fork(hotTask);

      // Wait for all tasks to complete
      scope.joinUntil(Instant.now().plus(Duration.ofSeconds(2)));

      // Handle child Task resuls (might have succeeded or failed)
      if (expSubTask.state() == StructuredTaskScope.Subtask.State.SUCCESS)
        log.info("Expirea task completed: {}", expSubTask.get());
      else if (expSubTask.state() == StructuredTaskScope.Subtask.State.FAILED)
        log.error("Expirea task failed: {}", expSubTask.exception());

      if (hotSubTask.state() == StructuredTaskScope.Subtask.State.SUCCESS)
        log.info("Hotwire task completed: {}", hotSubTask.get());
      else if (hotSubTask.state() == StructuredTaskScope.Subtask.State.FAILED)
        log.error("Hotwire task failed: {}", hotSubTask.exception());

    }
  }

}