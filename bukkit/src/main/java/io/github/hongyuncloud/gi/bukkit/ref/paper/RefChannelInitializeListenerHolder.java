package io.github.hongyuncloud.gi.bukkit.ref.paper;

import org.bukkit.NamespacedKey;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "io/papermc/paper/network/ChannelInitializeListenerHolder")
public final class RefChannelInitializeListenerHolder {
  @HandleBy(reference = "Lio/papermc/paper/network/ChannelInitializeListenerHolder;addListener(Lnet/kyori/adventure/key/Key;Lio/papermc/paper/network/ChannelInitializeListener;)V")
  public static native void addListener(NamespacedKey key, RefPaperChannelInitializeListener listener);

  @HandleBy(reference = "Lio/papermc/paper/network/ChannelInitializeListenerHolder;removeListener(Lnet/kyori/adventure/key/Key;)Lio/papermc/paper/network/ChannelInitializeListener;")
  public static native RefPaperChannelInitializeListener removeListener(NamespacedKey key);
}
