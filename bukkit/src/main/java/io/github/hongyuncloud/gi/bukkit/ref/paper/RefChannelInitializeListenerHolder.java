package io.github.hongyuncloud.gi.bukkit.ref.paper;

import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "io/papermc/paper/network/ChannelInitializeListenerHolder")
public final class RefChannelInitializeListenerHolder {
  @HandleBy(reference = "Lio/papermc/paper/network/ChannelInitializeListenerHolder;addListener(Lnet/kyori/adventure/key/Key;Lio/papermc/paper/network/ChannelInitializeListener;)V")
  public static native void addListener(@NonNull NamespacedKey key, @NonNull RefPaperChannelInitializeListener listener);

  @HandleBy(reference = "Lio/papermc/paper/network/ChannelInitializeListenerHolder;removeListener(Lnet/kyori/adventure/key/Key;)Lio/papermc/paper/network/ChannelInitializeListener;")
  public static native @Nullable RefPaperChannelInitializeListener removeListener(@NonNull NamespacedKey key);
}
