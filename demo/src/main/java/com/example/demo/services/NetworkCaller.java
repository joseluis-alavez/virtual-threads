package com.example.demo.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class NetworkCaller {

  private String callName;

  public String makeCall(int seconds) throws IOException {

    log.info("{} BEG call : {}", callName, Thread.currentThread());

    URI uri = URI.create("http://httpbin.org/delay/" + seconds);

    try {
      try (InputStream stream = uri.toURL().openStream()) {
        return new String(stream.readAllBytes());
      }
    } finally {
      log.info("{} END call : {}", callName, Thread.currentThread());
    }

  }

}