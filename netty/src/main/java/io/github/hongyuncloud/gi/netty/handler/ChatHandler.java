package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.GiUserConfig;
import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class ChatHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(ChatHandler.class);

  private final @NotNull GiCoreOutboundNettyHandler nettyCore;

  public ChatHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
  }

  @Override
  public void handle(final Wrapper wrapper) throws IOException {
    String content = wrapper.readVarString();
    String outputContent = nettyCore.core().component(GiUserConfig.empty(), content);
    wrapper.writeVarString(outputContent);
  }
}
