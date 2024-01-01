package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class PlayerInfoUpdateHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(PlayerInfoUpdateHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;


  public PlayerInfoUpdateHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    if (wrapper.copyByte() != 0x20) {
      wrapper.copyRemaining();
      return;
    }

    int playerCount = wrapper.copyVarInt();
    for (int i = 0; i < playerCount; i++) {
      wrapper.copyUUID();

      byte hasDisplayName = wrapper.readByte();
      wrapper.writeByte(hasDisplayName);
      if (hasDisplayName != 0) {
        chatHandler.handle(wrapper);
      }
    }
  }
}
