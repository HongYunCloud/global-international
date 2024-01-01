package io.github.hongyuncloud.gi.netty;

import io.github.hongyuncloud.gi.GiCore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GiCoreInbountNettyHandler extends ChannelDuplexHandler implements ChannelOutboundHandler {
  public static final @NotNull AttributeKey<@Nullable Integer> CURRENT_STATE_KEY = AttributeKey.valueOf("gi_current_state");
  private static final @NotNull Logger logger = LoggerFactory.getLogger(GiCoreInbountNettyHandler.class);

  private final @NotNull GiCore core;

  public GiCoreInbountNettyHandler(final @NotNull GiCore core) {
    this.core = core;
  }

  public @NotNull GiCore core() {
    return core;
  }

  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object rawMsg) throws Exception {
    Integer rawCurrentState = ctx.channel().attr(CURRENT_STATE_KEY).get();
    int currentState = (rawCurrentState == null) ? 0 : rawCurrentState;

    ByteBuf msg = (ByteBuf) rawMsg;
    int startIndex = msg.readerIndex();

    msg.retain();
    try {
      if (currentState == 0 || currentState == 2 || currentState == 3) {
        int packetId = VarInt.read(msg);
        if (currentState == 0 && packetId == 0x00) {
          VarInt.read(msg);
          int serverAddressSize = VarInt.read(msg);
          msg.skipBytes(serverAddressSize);
          msg.readUnsignedShort();
          int nextStatus = VarInt.read(msg);
          ctx.channel().attr(CURRENT_STATE_KEY).set(nextStatus);
          logger.info("set status to {}", nextStatus);
        } else if (currentState == 2 && packetId == 0x03) {
          ctx.channel().attr(CURRENT_STATE_KEY).set(3);
          logger.info("set status to {}", 3);
        } else if (currentState == 3 && packetId == 0x02) {
          ctx.channel().attr(CURRENT_STATE_KEY).set(4);
          logger.info("set status to {}", 4);
        }
      }
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.error("Error while handling inbound packet\n{}", ByteBufUtil.prettyHexDump(msg), e);
      } else {
        logger.error("Error while handling inbound packet", e);
      }
    } finally {
      msg.release();
    }

    msg.readerIndex(startIndex);
    ctx.fireChannelRead(msg);
  }
}
