package io.github.hongyuncloud.gi.bukkit;

import io.github.hongyuncloud.gi.bukkit.ref.paper.RefChannelInitializeListenerHolder;
import io.github.hongyuncloud.gi.netty.GiCoreInbountNettyHandler;
import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class PaperInjector {
  private static final @NotNull NamespacedKey KEY = new NamespacedKey("gi", "gi-channel-initialize-listener");

  private PaperInjector() {
    throw new UnsupportedOperationException();
  }

  public static void attach() {
    RefChannelInitializeListenerHolder.addListener(KEY, channel -> {
      channel.pipeline().addBefore("decoder", "gi-channel-inbound", new GiCoreInbountNettyHandler(BukkitGiCore.core()));
      channel.pipeline().addBefore("encoder", "gi-channel-outbound", new GiCoreOutboundNettyHandler(BukkitGiCore.core()));
    });
  }

  public static void unattach() {
    RefChannelInitializeListenerHolder.removeListener(KEY);
  }
}
