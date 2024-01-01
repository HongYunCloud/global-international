package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class FallbackHandler implements IHandler {
  public static final @NotNull FallbackHandler INSTANCE = new FallbackHandler();

  private FallbackHandler() {
    //
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyRemaining();
  }
}
