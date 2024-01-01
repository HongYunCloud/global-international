package io.github.hongyuncloud.gi.bukkit.ref.paper;

import io.netty.channel.Channel;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.inksnow.ankhinvoke.comments.HandleBy;

@FunctionalInterface
@HandleBy(reference = "io/papermc/paper/network/ChannelInitializeListener")
public interface RefPaperChannelInitializeListener {
  @HandleBy(reference = "Lio/papermc/paper/network/ChannelInitializeListener;afterInitChannel(Lio/netty/channel/Channel;)V")
  void afterInitChannel(@NonNull Channel channel);
}
