package com.example.demo.structuredconcurrency;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {

  private String name;
  private String response;
  private long timeTakens;

}