package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.GiUserConfig;
import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.GiNbt;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import net.kyori.adventure.nbt.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class SlotHandle implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(SlotHandle.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;

  public SlotHandle(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    if (wrapper.copyByte() != 0) {
      wrapper.copyVarInt();
      wrapper.copyByte();

      CompoundBinaryTag slot = GiNbt.readUnnamedTag(wrapper.source());

      if (slot != null) {
        if (logger.isTraceEnabled()) {
          logger.trace("slot: {}", TagStringIO.builder()
              .indent(2)
              .build()
              .asString(slot));
        }
        // display
        CompoundBinaryTag displayCompound = slot.getCompound("display", null);
        if (displayCompound != null) {
          displayCompound = wrapperNbtComponent(displayCompound, "Name");
          displayCompound = wrapperNbtComponent(displayCompound, "Lore");
          slot = slot.put("display", displayCompound);
        }

        // book
        slot = wrapperNbtText(slot, "author");
        slot = wrapperNbtText(slot, "title");
        ListBinaryTag pages = slot.getList("pages", BinaryTagTypes.STRING, null);
        if (pages != null) {
          for (int i = 0; i < pages.size(); i++) {
            String content = pages.getString(i);
            String outputContent = nettyCore.core().component(GiUserConfig.empty(), content);
            pages = pages.set(i, StringBinaryTag.stringBinaryTag(outputContent), null);
          }
          slot = slot.put("pages", pages);
        }
      }

      GiNbt.writeUnnamedTag(wrapper.target(), slot);
    }
  }

  private @NotNull CompoundBinaryTag wrapperNbtComponent(final @NotNull CompoundBinaryTag compound, @NotNull String key) {
    String content = compound.getString(key, null);
    if (content != null) {
      String outputContent = nettyCore.core().component(GiUserConfig.empty(), content);
      return compound.putString(key, outputContent);
    }
    return compound;
  }

  private @NotNull CompoundBinaryTag wrapperNbtText(final @NotNull CompoundBinaryTag compound, @NotNull String key) {
    String content = compound.getString(key, null);
    if (content != null) {
      String outputContent = nettyCore.core().text(GiUserConfig.empty(), content);
      return compound.putString(key, outputContent);
    }
    return compound;
  }
}
