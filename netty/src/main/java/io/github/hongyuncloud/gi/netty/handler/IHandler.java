package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;

import java.io.IOException;

public interface IHandler {
  void handle(Wrapper wrapper) throws IOException;
}
