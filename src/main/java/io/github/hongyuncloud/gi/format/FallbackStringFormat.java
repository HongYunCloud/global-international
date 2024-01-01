package io.github.hongyuncloud.gi.format;

import io.github.hongyuncloud.gi.GiUserConfig;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public final class FallbackStringFormat implements TranslateFormat {
  private final @NotNull String fallback;

  private FallbackStringFormat(final @NotNull String fallback) {
    this.fallback = fallback;
  }

  public static @NotNull FallbackStringFormat of(final @NotNull String fallback) {
    return new FallbackStringFormat(fallback);
  }

  @Override
  public void applyTranslate(final @NotNull GiUserConfig context, final @NotNull TranslatableComponent component, final TranslatableComponent.@NotNull Builder builder) {
    builder.fallback(fallback);
  }
}
