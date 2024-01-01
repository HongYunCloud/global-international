package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class UpdateAdvancementsHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(UpdateAdvancementsHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;
  private final @NotNull SlotHandle slotHandle;

  public UpdateAdvancementsHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
    this.slotHandle = nettyCore.typeHandler(SlotHandle.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyByte();
    int mappingSize = wrapper.copyVarInt();
    for (int i = 0; i < mappingSize; i++) {
      wrapper.copyVarString();
      if (wrapper.copyByte() != 0) {
        wrapper.copyVarString();
      }
      if (wrapper.copyByte() != 0) {
        // inline Display data
        chatHandler.handle(wrapper);
        chatHandler.handle(wrapper);
        slotHandle.handle(wrapper);
        wrapper.copyVarInt();
        if ((wrapper.copyInt() & 0x01) != 0) {
          wrapper.copyVarString();
        }
        wrapper.copyFloat();
        wrapper.copyFloat();
      }
      int arrayLength = wrapper.copyVarInt();
      for (int j = 0; j < arrayLength; j++) {
        int arrayLength2 = wrapper.copyVarInt();
        for (int k = 0; k < arrayLength2; k++) {
          wrapper.copyVarString();
        }
      }
      wrapper.copyByte();
    }
    int listSize = wrapper.copyVarInt();
    for (int i = 0; i < listSize; i++) {
      wrapper.copyVarString();
    }
    int progressSize = wrapper.copyVarInt();
    for (int i = 0; i < progressSize; i++) {
      wrapper.copyVarString();
      int size = wrapper.copyVarInt();
      for (int j = 0; j < size; j++) {
        wrapper.copyVarString();
        if (wrapper.copyByte() != 0) {
          wrapper.copyLong();
        }
      }
    }
  }
}
