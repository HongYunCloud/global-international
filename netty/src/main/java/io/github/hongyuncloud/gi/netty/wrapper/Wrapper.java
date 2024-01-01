package io.github.hongyuncloud.gi.netty.wrapper;

import io.github.hongyuncloud.gi.GiUserConfig;
import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.VarInt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class Wrapper {
  private final @NotNull ChannelHandlerContext ctx;
  private final @NotNull ByteBuf source;
  private final @NotNull ByteBuf target;

  public Wrapper(final @NotNull ChannelHandlerContext ctx, final @NotNull ByteBuf source, final @NotNull ByteBuf target) {
    this.ctx = ctx;
    this.source = source;
    this.target = target;
  }

  public @NotNull ChannelHandlerContext ctx() {
    return ctx;
  }

  public @NotNull ByteBuf source() {
    return source;
  }

  public @NotNull ByteBuf target() {
    return target;
  }

  public @NotNull GiUserConfig userConfig() {
    GiUserConfig result = ctx.channel()
        .attr(GiCoreOutboundNettyHandler.USER_CONFIG_KEY)
        .get();
    return result == null ? GiUserConfig.empty() : result;
  }

  public void copyRemaining() {
    target.writeBytes(source);
  }

  public byte readByte() {
    return source.readByte();
  }

  public void writeByte(byte value) {
    target.writeByte(value);
  }

  public int copyInt() {
    int value = source.readInt();
    target.writeInt(value);
    return value;
  }

  public long copyLong() {
    long value = source.readLong();
    target.writeLong(value);
    return value;
  }

  public byte copyByte() {
    byte value = source.readByte();
    target.writeByte(value);
    return value;
  }

  public short copyShort() {
    short value = source.readShort();
    target.writeShort(value);
    return value;
  }

  public float copyFloat() {
    float value = source.readFloat();
    target.writeFloat(value);
    return value;
  }

  public int readVarInt() {
    return VarInt.read(source);
  }

  public void writeVarInt(int value) {
    VarInt.write(target, value);
  }

  public int copyVarInt() {
    int value = VarInt.read(source);
    VarInt.write(target, value);
    return value;
  }

  public byte[] copyBytes(final int length) {
    byte[] value = new byte[length];
    source.readBytes(value);
    target.writeBytes(value);
    return value;
  }

  public byte[] copyVarBytes() {
    int length = copyVarInt();
    return copyBytes(length);
  }

  public String copyVarString() {
    return new String(copyVarBytes(), StandardCharsets.UTF_8);
  }

  public @NotNull String readVarString() {
    int length = VarInt.read(source);
    byte[] bytes = new byte[length];
    source.readBytes(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public void writeVarString(final @NotNull String content) {
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
    VarInt.write(target, bytes.length);
    target.writeBytes(bytes);
  }

  public void copyUUID() {
    target.writeBytes(source, 16);
  }

  public void copyVarBitSet() {
    copyBytes(copyVarInt() * 8);
  }
}
