package io.github.hongyuncloud.gi.netty;

import io.netty.buffer.ByteBuf;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class VarInt {
  private static final int MAX_VARINT_SIZE = 5;
  private static final int DATA_BITS_MASK = 127;
  private static final int CONTINUATION_BIT_MASK = 128;
  private static final int DATA_BITS_PER_BYTE = 7;
  private static final int[] VARINT_EXACT_BYTE_LENGTHS = new int[33];

  static {
    for (int i = 0; i <= 32; ++i) {
      VARINT_EXACT_BYTE_LENGTHS[i] = (int) Math.ceil((31d - (i - 1)) / 7d);
    }
    VARINT_EXACT_BYTE_LENGTHS[32] = 1; // Special case for the number 0.
  }

  public static int getByteSize(int i) {
    // Paper start - Optimize VarInts
    return VARINT_EXACT_BYTE_LENGTHS[Integer.numberOfLeadingZeros(i)];
  }

  public static int getByteSizeOld(int i) {
    //Paper end - Optimize VarInts
    for (int j = 1; j < 5; ++j) {
      if ((i & -1 << j * 7) == 0) {
        return j;
      }
    }

    return 5;
  }

  public static boolean hasContinuationBit(byte b) {
    return (b & 128) == 128;
  }

  public static int read(ByteBuf buf) {
    int i = 0;
    int j = 0;

    byte b;
    do {
      b = buf.readByte();
      i |= (b & 127) << j++ * 7;
      if (j > 5) {
        throw new RuntimeException("VarInt too big");
      }
    } while (hasContinuationBit(b));

    return i;
  }

  public static int read(DataInput buf) throws IOException {
    int i = 0;
    int j = 0;

    byte b;
    do {
      b = buf.readByte();
      i |= (b & 127) << j++ * 7;
      if (j > 5) {
        throw new RuntimeException("VarInt too big");
      }
    } while (hasContinuationBit(b));

    return i;
  }

  public static int read(InputStream buf) throws IOException {
    int i = 0;
    int j = 0;

    int rB;
    byte b;
    do {
      rB = buf.read();
      if (rB < 0) throw new EOFException("Reached end of stream");
      b = (byte) rB;
      i |= (b & 127) << j++ * 7;
      if (j > 5) {
        throw new RuntimeException("VarInt too big");
      }
    } while (hasContinuationBit(b));

    return i;
  }

  public static ByteBuf write(ByteBuf buf, int i) {
    // Paper start - Optimize VarInts
    // Peel the one and two byte count cases explicitly as they are the most common VarInt sizes
    // that the proxy will write, to improve inlining.
    if ((i & (0xFFFFFFFF << 7)) == 0) {
      buf.writeByte(i);
    } else if ((i & (0xFFFFFFFF << 14)) == 0) {
      int w = (i & 0x7F | 0x80) << 8 | (i >>> 7);
      buf.writeShort(w);
    } else {
      writeOld(buf, i);
    }
    return buf;
  }

  public static ByteBuf writeOld(ByteBuf buf, int i) {
    // Paper end - Optimize VarInts
    while ((i & -128) != 0) {
      buf.writeByte(i & 127 | 128);
      i >>>= 7;
    }

    buf.writeByte(i);
    return buf;
  }
}
