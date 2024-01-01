package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class UpdateTeamsHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(UpdateTeamsHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;

  public UpdateTeamsHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyVarString();

    byte mode = wrapper.copyByte();
    if (mode == 0 || mode == 2) {
      chatHandler.handle(wrapper);
      wrapper.copyByte();
      wrapper.copyVarString();
      wrapper.copyVarString();
      wrapper.copyVarInt();
      chatHandler.handle(wrapper);
      chatHandler.handle(wrapper);
    }
    wrapper.copyRemaining();
  }
}
