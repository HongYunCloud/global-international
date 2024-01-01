package io.github.hongyuncloud.gi.netty.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class StatusResponseHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(StatusResponseHandler.class);
  private static final @NotNull Gson gson = new GsonBuilder()
      .disableHtmlEscaping()
      .create();
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;

  public StatusResponseHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    JsonObject jsonObject = gson.fromJson(wrapper.readVarString(), JsonObject.class);

    String rawComponent = gson.toJson(jsonObject.get("description"));
    String mappedComponent = nettyCore.core().component(wrapper.userConfig(), rawComponent);

    jsonObject.add("description", gson.fromJson(mappedComponent, JsonElement.class));

    wrapper.writeVarString(gson.toJson(jsonObject));
  }
}
