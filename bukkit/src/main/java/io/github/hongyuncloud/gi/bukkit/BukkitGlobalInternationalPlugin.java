package io.github.hongyuncloud.gi.bukkit;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.inksnow.ankhinvoke.AnkhInvoke;
import org.inksnow.ankhinvoke.bukkit.AnkhInvokeBukkit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class BukkitGlobalInternationalPlugin extends JavaPlugin implements Listener {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(BukkitGlobalInternationalPlugin.class);

  static {
    AnkhInvoke ankhInvoke = AnkhInvokeBukkit.forBukkit(BukkitGlobalInternationalPlugin.class)
        .reference()
        /**/.appendPackage("io.github.hongyuncloud.gi.bukkit.ref")
        /**/.build()
        .inject()
        /**/.unsafeInjector(BukkitGlobalInternationalPlugin.class.getClassLoader())
        /**/.build()
        .referenceRemap()
        /**/.setApplyMapRegistry("global-international")
        /**/.build()
        .build();
    ankhInvoke.get("io.github.hongyuncloud.gi.bukkit.PaperInjector");
  }

  public BukkitGlobalInternationalPlugin() {
    BukkitGiCore.instancePlugin(this);
  }

  public BukkitGlobalInternationalPlugin(
      final @NotNull JavaPluginLoader loader,
      final @NotNull PluginDescriptionFile description,
      final @NotNull File dataFolder,
      final @NotNull File file) {
    super(loader, description, dataFolder, file);
    BukkitGiCore.instancePlugin(this);
  }

  @Override
  public void onLoad() {
    BukkitGiCore.load();
  }

  @Override
  public void onEnable() {
    BukkitGiCore.enable();
  }

  @Override
  public void onDisable() {
    BukkitGiCore.disable();
  }
}
