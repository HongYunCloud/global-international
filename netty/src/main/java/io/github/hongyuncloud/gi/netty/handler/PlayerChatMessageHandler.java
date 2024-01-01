package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class PlayerChatMessageHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(PlayerChatMessageHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;

  public PlayerChatMessageHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    // Header
    wrapper.copyUUID();
    wrapper.copyVarInt();
    byte messageSignaturePresent = wrapper.copyByte();
    if (messageSignaturePresent != 0) {
      wrapper.copyBytes(256);
    }

    // Body
    wrapper.copyVarString();
    wrapper.copyLong();
    wrapper.copyLong();

    // Previous Messages
    int totalPreviousMessages = wrapper.copyVarInt();
    for (int i = 0; i < totalPreviousMessages; i++) {
      wrapper.copyVarInt();
      if (messageSignaturePresent != 0) {
        wrapper.copyBytes(256);
      }
    }

    // Other
    if (wrapper.copyByte() != 0) {
      chatHandler.handle(wrapper);
    }
    int filterType = wrapper.copyVarInt();
    if (filterType == 2) {
      wrapper.copyVarBitSet();
    }

    // Chat Formatting
    wrapper.copyVarInt();
    chatHandler.handle(wrapper);
    if (wrapper.copyByte() != 0) {
      chatHandler.handle(wrapper);
    }
  }
}
