package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class SystemChatMessageHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(SystemChatMessageHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;

  public SystemChatMessageHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    chatHandler.handle(wrapper);
    wrapper.copyByte();
  }
}
