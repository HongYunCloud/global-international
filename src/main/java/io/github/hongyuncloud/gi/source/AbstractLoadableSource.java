package io.github.hongyuncloud.gi.source;

import io.github.hongyuncloud.gi.format.TranslateFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLoadableSource<T, F extends TranslateFormat> implements TranslateSource {
  private final Object updateLock = new Object();
  private final @NotNull Map<@NotNull String, T> dataMap = new ConcurrentHashMap<>();
  private final @NotNull Map<@NotNull String, F> formatMap = new ConcurrentHashMap<>();
  private volatile boolean loaded;

  protected abstract void load();

  protected abstract F createFormat(T data);

  protected void set(final @NotNull String key, final @NotNull T data) {
    synchronized (updateLock) {
      dataMap.put(key, data);
      formatMap.remove(key);
    }
  }

  @Override
  public @Nullable F provide(final @NotNull String locale, final @NotNull String key) {
    if (!loaded) {
      synchronized (updateLock) {
        if (!loaded) {
          load();
          loaded = true;
        }
      }
    }

    String cacheKey = locale + "." + key;
    F format = formatMap.get(cacheKey);
    if (format == null) {
      synchronized (updateLock) {
        format = formatMap.get(cacheKey);
        if (format == null) {
          T data = dataMap.get(cacheKey);
          if (data == null) {
            return null;
          } else {
            format = createFormat(data);
            formatMap.put(cacheKey, format);
            return format;
          }
        }
      }
    }
    return format;
  }
}
