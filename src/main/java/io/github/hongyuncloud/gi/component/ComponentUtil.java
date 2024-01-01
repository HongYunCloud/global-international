package io.github.hongyuncloud.gi.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ComponentUtil {
  private ComponentUtil() {
    throw new UnsupportedOperationException();
  }

  public static @NotNull JsonObject text(@Nullable String text) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("text", text == null ? "" : text);
    return jsonObject;
  }

  public static @NotNull JsonObject merge(@Nullable JsonElement element) {
    if (element == null || element.isJsonNull()) {
      return text(null);
    } else if (element.isJsonObject()) {
      return element.getAsJsonObject();
    } else if (element.isJsonArray()) {
      JsonObject jsonObject = text(null);
      jsonObject.add("extra", element);
      return jsonObject;
    } else {
      return text(element.getAsString());
    }
  }
}
