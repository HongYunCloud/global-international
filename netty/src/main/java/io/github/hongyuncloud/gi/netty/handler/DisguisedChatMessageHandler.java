package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class DisguisedChatMessageHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(DisguisedChatMessageHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;

  public DisguisedChatMessageHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);

  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    chatHandler.handle(wrapper);
    wrapper.copyVarInt();
    chatHandler.handle(wrapper);
    if (wrapper.copyByte() != 0) {
      chatHandler.handle(wrapper);
    }
  }
}
