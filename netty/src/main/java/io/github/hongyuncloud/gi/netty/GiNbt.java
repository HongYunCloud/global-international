package io.github.hongyuncloud.gi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBufUtil;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.EndBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class GiNbt {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(GiNbt.class);

  private GiNbt() {
    throw new UnsupportedOperationException();
  }

  public static @Nullable CompoundBinaryTag readUnnamedTag(final @NotNull ByteBuf byteBuf) throws IOException {
    int startIndex = byteBuf.readerIndex();
    try (ByteBufInputStream dataIn = new ByteBufInputStream(byteBuf)) {
      final byte type = dataIn.readByte();
      if (type == BinaryTagTypes.END.id()) {
        return null;
      } else if (type == BinaryTagTypes.COMPOUND.id()) {
        return BinaryTagTypes.COMPOUND.read(dataIn);
      } else {
        throw new IOException(String.format("Expected root tag to be a %s, was %s", BinaryTagTypes.COMPOUND, type));
      }
    } catch (IOException e) {
      if (logger.isDebugEnabled()) {
        byteBuf.readerIndex(startIndex);
        logger.debug("failed to read nbt in {}\n{}", byteBuf, ByteBufUtil.prettyHexDump(byteBuf), e);
      }
      throw e;
    }
  }

  public static void writeUnnamedTag(final @NotNull ByteBuf byteBuf, final @Nullable CompoundBinaryTag tag) throws IOException {
    try (ByteBufOutputStream dataOut = new ByteBufOutputStream(byteBuf)) {
      if (tag == null) {
        dataOut.writeByte(BinaryTagTypes.END.id());
        BinaryTagTypes.END.write(EndBinaryTag.endBinaryTag(), dataOut);
      } else {
        dataOut.writeByte(BinaryTagTypes.COMPOUND.id());
        BinaryTagTypes.COMPOUND.write(tag, dataOut);
      }
    }
  }
}
