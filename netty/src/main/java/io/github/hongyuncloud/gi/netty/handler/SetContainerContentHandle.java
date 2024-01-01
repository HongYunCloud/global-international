package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public final class SetContainerContentHandle implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(SetContainerContentHandle.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull SlotHandle slotHandle;

  public SetContainerContentHandle(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.slotHandle = Objects.requireNonNull(nettyCore.typeHandler(SlotHandle.class));
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    wrapper.copyByte();
    wrapper.copyVarInt();

    int count = wrapper.copyVarInt();

    for (int i = 0; i <= count; i++) { // include one slot for cursor
      slotHandle.handle(wrapper);
    }
  }
}
