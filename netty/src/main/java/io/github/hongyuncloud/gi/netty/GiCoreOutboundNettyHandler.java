package io.github.hongyuncloud.gi.netty;

import io.github.hongyuncloud.gi.GiCore;
import io.github.hongyuncloud.gi.GiUserConfig;
import io.github.hongyuncloud.gi.netty.handler.*;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class GiCoreOutboundNettyHandler extends ChannelDuplexHandler implements ChannelOutboundHandler {
  public static final @NotNull AttributeKey<@Nullable GiUserConfig> USER_CONFIG_KEY = AttributeKey.valueOf("gi_user_config");
  private static final @NotNull Logger logger = LoggerFactory.getLogger(GiCoreOutboundNettyHandler.class);

  private final @NotNull GiCore core;
  private final @NotNull Int2ObjectMap<@NotNull IHandler>[] handlers = new Int2ObjectMap[5];
  private final @NotNull Object2ObjectMap<@NotNull Class<?>, @NotNull IHandler> typeHandlers = new Object2ObjectOpenHashMap<>();

  public GiCoreOutboundNettyHandler(final @NotNull GiCore core) {
    this.core = core;

    for (int i = 0; i < handlers.length; i++) {
      handlers[i] = new Int2ObjectAVLTreeMap<>();
    }

    typeHandlers.put(SlotHandle.class, new SlotHandle(this));
    typeHandlers.put(ChatHandler.class, new ChatHandler(this));

    // Handshake

    // Status
    handlers[1].put(0x00, new StatusResponseHandler(this));

    // Login
    handlers[2].put(0x00, new DisconnectLoginHandler(this));

    // Configuration
    handlers[3].put(0x01, new DisconnectLoginHandler(this));
    handlers[3].put(0x06, new ResourcePackHandler(this));

    // Play
    handlers[4].put(0x0A, new BossBarHandler(this));
    handlers[4].put(0x10, new CommandSuggestionsHandler(this));
    handlers[4].put(0x13, new SetContainerContentHandle(this));
    handlers[4].put(0x15, new SetContainerSlotHandle(this));
    handlers[4].put(0x1B, new DisconnectLoginHandler(this));
    handlers[4].put(0x1C, new DisguisedChatMessageHandler(this));
    handlers[4].put(0x2A, new MapDataHandler(this));
    handlers[4].put(0x2B, new MerchantOffersHandle(this));
    handlers[4].put(0x31, new OpenScreenHandler(this));
    handlers[4].put(0x37, new PlayerChatMessageHandler(this));
    handlers[4].put(0x3A, new CombatDeathHandler(this));
    handlers[4].put(0x3C, new PlayerInfoUpdateHandler(this));
    handlers[4].put(0x42, new ResourcePackHandler(this));
    handlers[4].put(0x47, new ServerDataHandler(this));
    handlers[4].put(0x48, new SetActionBarTextHandler(this));
    handlers[4].put(0x57, new SetEquipmentContentHandle(this));
    handlers[4].put(0x5A, new UpdateObjectivesHandler(this));
    handlers[4].put(0x5C, new UpdateTeamsHandler(this));
    handlers[4].put(0x5F, new SetSubtitleTextHandler(this));
    handlers[4].put(0x61, new SetTitleTextHandler(this));
    handlers[4].put(0x67, new SystemChatMessageHandler(this));
    handlers[4].put(0x68, new SetTabListHeaderAndFooterHandle(this));
    handlers[4].put(0x6C, new UpdateAdvancementsHandler(this));
    handlers[4].put(0x6F, new UpdateRecipesHandler(this));
  }

  public @NotNull GiCore core() {
    return core;
  }

  public @NotNull <T extends IHandler> T typeHandler(final @NotNull Class<T> type) {
    return (T) Objects.requireNonNull(typeHandlers.get(type));
  }

  @Override
  public void write(final ChannelHandlerContext ctx, final Object rawMsg, final ChannelPromise promise) throws Exception {
    Integer rawCurrentState = ctx.channel().attr(GiCoreInbountNettyHandler.CURRENT_STATE_KEY).get();
    int currentState = (rawCurrentState == null) ? 0 : rawCurrentState;

    ByteBuf msg = (ByteBuf) rawMsg;
    int startIndex = msg.readerIndex();

    final ByteBuf transformedBuf = ctx.alloc().buffer();
    try {
      final int packetId = VarInt.read(msg);
      VarInt.write(transformedBuf, packetId);

      Wrapper wrapper = new Wrapper(ctx, msg, transformedBuf);
      IHandler handler = handlers[currentState].getOrDefault(packetId, FallbackHandler.INSTANCE);
      try {
        handler.handle(wrapper);
        ctx.write(transformedBuf.retain(), promise);
      } catch (Throwable e) {
        msg.readerIndex(startIndex);
        if (logger.isDebugEnabled()) {
          logger.error("Error while handling packet {} with {}\n{}", packetId, handler, ByteBufUtil.prettyHexDump(msg), e);
        } else {
          logger.error("Error while handling packet {} with {}", packetId, handler, e);
        }
        if (e instanceof Error) {
          throw e;
        } else {
          ctx.write(msg, promise);
        }
      }
    } finally {
      transformedBuf.release();
    }
  }
}
