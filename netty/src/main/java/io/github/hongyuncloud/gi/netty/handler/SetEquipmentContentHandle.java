package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public final class SetEquipmentContentHandle implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(SetEquipmentContentHandle.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull SlotHandle slotHandle;

  public SetEquipmentContentHandle(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.slotHandle = Objects.requireNonNull(nettyCore.typeHandler(SlotHandle.class));
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyVarInt();

    while (wrapper.source().isReadable()) {
      wrapper.copyByte();
      slotHandle.handle(wrapper);
    }
  }
}
