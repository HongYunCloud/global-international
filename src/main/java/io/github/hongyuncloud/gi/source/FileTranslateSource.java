package io.github.hongyuncloud.gi.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.hongyuncloud.gi.format.FallbackStringFormat;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public final class FileTranslateSource extends AbstractLoadableSource<String, FallbackStringFormat> {
  private static final @NotNull Gson gson = new GsonBuilder()
      .disableHtmlEscaping()
      .create();
  private final @NotNull Path rootDirectory;

  public FileTranslateSource(final @NotNull Path rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  @Override
  protected void load() {
    try {
      scanDirectory(rootDirectory, "");
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void scanDirectory(final @NotNull Path path, final @NotNull String keyPrefix) throws IOException {
    try (Stream<Path> stream = Files.list(path)) {
      Iterator<Path> iterator = stream.iterator();
      while (iterator.hasNext()) {
        Path subPath = iterator.next();
        String fileName = subPath.getFileName().toString();
        if (Files.isDirectory(subPath)) {
          scanDirectory(subPath, keyPrefix + (keyPrefix.isEmpty() ? "" : ".") + fileName);
        } else if (fileName.endsWith(".json")) {
          scanFile(subPath, keyPrefix + (keyPrefix.isEmpty() ? "" : ".") + fileName.substring(0, fileName.length() - ".json".length()));
        }
      }
    }
  }

  private void scanFile(final @NotNull Path path, final @NotNull String keyPrefix) throws IOException {
    JsonElement jsonElement;
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      jsonElement = gson.fromJson(reader, JsonElement.class);
    }
    scanElement(jsonElement, keyPrefix);
  }

  private void scanElement(final @NotNull JsonElement jsonElement, final @NotNull String keyPrefix) {
    if (jsonElement.isJsonObject()) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      for (String entryName : jsonObject.keySet()) {
        scanElement(jsonObject.get(entryName), keyPrefix + (keyPrefix.isEmpty() ? "" : ".") + entryName);
      }
    } else {
      set(keyPrefix, jsonElement.getAsString());
    }
  }

  @Override
  protected FallbackStringFormat createFormat(final String data) {
    return FallbackStringFormat.of(data);
  }
}
