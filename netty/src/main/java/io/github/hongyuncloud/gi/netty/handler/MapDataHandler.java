package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class MapDataHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(MapDataHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;

  public MapDataHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyVarInt();
    wrapper.copyByte();
    wrapper.copyByte();

    if (wrapper.copyByte() != 0) {
      int iconCount = wrapper.copyVarInt();

      for (int i = 0; i < iconCount; i++) {
        wrapper.copyVarInt();
        wrapper.copyByte();
        wrapper.copyByte();
        wrapper.copyByte();

        if (wrapper.copyByte() != 0) {
          chatHandler.handle(wrapper);
        }
      }
    }
    wrapper.copyRemaining();
  }
}
