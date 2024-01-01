package io.github.hongyuncloud.gi.format;

import io.github.hongyuncloud.gi.GiUserConfig;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public interface TranslateFormat {
  void applyTranslate(@NotNull GiUserConfig context, @NotNull TranslatableComponent component, TranslatableComponent.@NotNull Builder builder);
}
