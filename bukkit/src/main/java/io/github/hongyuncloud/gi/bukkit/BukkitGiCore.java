package io.github.hongyuncloud.gi.bukkit;

import io.github.hongyuncloud.gi.GiCore;
import io.github.hongyuncloud.gi.source.FileTranslateSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

public final class BukkitGiCore {
  public static final @NotNull String NOT_LOADED_TEXT = "GlobalInternational still not loaded";
  public static final @NotNull String ALREADY_LOADED_TEXT = "GlobalInternational already loaded";
  private static final @NotNull Logger logger = LoggerFactory.getLogger(BukkitGiCore.class);

  private static final @NotNull AtomicReference<@Nullable BukkitGlobalInternationalPlugin> plugin = new AtomicReference<>();
  private static final @NotNull AtomicReference<@Nullable GiCore> core = new AtomicReference<>();

  private BukkitGiCore() {
    throw new UnsupportedOperationException();
  }

  public static @NotNull BukkitGlobalInternationalPlugin plugin() {
    final BukkitGlobalInternationalPlugin pluginCopy = plugin.get();
    if (pluginCopy == null) {
      throw new IllegalStateException(NOT_LOADED_TEXT);
    }
    return pluginCopy;
  }

  public static @NotNull GiCore core() {
    final GiCore coreCopy = core.get();
    if (coreCopy == null) {
      throw new IllegalStateException(NOT_LOADED_TEXT);
    }
    return coreCopy;
  }

  /* package-private */
  static void instancePlugin(final @NotNull BukkitGlobalInternationalPlugin plugin) {
    final boolean loadResult = BukkitGiCore.plugin.compareAndSet(null, plugin);
    if (!loadResult) {
      throw new IllegalStateException(ALREADY_LOADED_TEXT);
    }
  }

  /* package-private */
  static void load() {
    final boolean loadResult = core.compareAndSet(null, new GiCore(
        new FileTranslateSource(Paths.get("i18n"))
    ));
    if (!loadResult) {
      throw new IllegalStateException(ALREADY_LOADED_TEXT);
    }
  }

  /* package-private */
  static void enable() {
    PaperInjector.attach();
  }

  /* package-private */
  static void disable() {
    PaperInjector.unattach();
  }
}
