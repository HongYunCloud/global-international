package io.github.hongyuncloud.gi.source;

import io.github.hongyuncloud.gi.format.TranslateFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslateSource {
  @Nullable TranslateFormat provide(@NotNull String locale, @NotNull String key);
}
