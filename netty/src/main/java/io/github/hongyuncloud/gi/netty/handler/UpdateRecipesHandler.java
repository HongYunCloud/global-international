package io.github.hongyuncloud.gi.netty.handler;

import io.github.hongyuncloud.gi.netty.GiCoreOutboundNettyHandler;
import io.github.hongyuncloud.gi.netty.wrapper.Wrapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class UpdateRecipesHandler implements IHandler {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(UpdateRecipesHandler.class);
  private final @NotNull GiCoreOutboundNettyHandler nettyCore;
  private final @NotNull ChatHandler chatHandler;
  private final @NotNull SlotHandle slotHandle;

  public UpdateRecipesHandler(final @NotNull GiCoreOutboundNettyHandler nettyCore) {
    this.nettyCore = nettyCore;
    this.chatHandler = nettyCore.typeHandler(ChatHandler.class);
    this.slotHandle = nettyCore.typeHandler(SlotHandle.class);
  }

  @Override
  public void handle(final @NotNull Wrapper wrapper) throws IOException {
    int numRecipes = wrapper.copyVarInt();
    for (int i = 0; i < numRecipes; i++) {
      String type = wrapper.copyVarString();
      wrapper.copyVarString();
      switch (type) {
        case "minecraft:crafting_shapeless": {
          wrapper.copyVarString();
          wrapper.copyVarInt();
          int ingredientCount = wrapper.copyVarInt();
          for (int j = 0; j < ingredientCount; j++) {
            handleIngredient(wrapper);
          }
          slotHandle.handle(wrapper);
          break;
        }
        case "minecraft:crafting_shaped": {
          int width = wrapper.copyVarInt();
          int height = wrapper.copyVarInt();
          wrapper.copyVarString();
          wrapper.copyVarInt();
          int ingredientCount = width * height;
          for (int j = 0; j < ingredientCount; j++) {
            handleIngredient(wrapper);
          }
          slotHandle.handle(wrapper);
          wrapper.copyByte();
          break;
        }
        case "minecraft:crafting_special_armordye":
        case "minecraft:crafting_special_bookcloning":
        case "minecraft:crafting_special_mapcloning":
        case "minecraft:crafting_special_mapextending":
        case "minecraft:crafting_special_firework_rocket":
        case "minecraft:crafting_special_firework_star":
        case "minecraft:crafting_special_firework_star_fade":
        case "minecraft:crafting_special_repairitem":
        case "minecraft:crafting_special_tippedarrow":
        case "minecraft:crafting_special_bannerduplicate":
        case "minecraft:crafting_special_shielddecoration":
        case "minecraft:crafting_special_shulkerboxcoloring":
        case "minecraft:crafting_special_suspiciousstew":
        case "minecraft:crafting_decorated_pot": {
          wrapper.copyVarInt();
          break;
        }
        case "minecraft:smelting":
        case "minecraft:blasting":
        case "minecraft:smoking":
        case "minecraft:campfire_cooking": {
          wrapper.copyVarString();
          wrapper.copyVarInt();
          handleIngredient(wrapper);
          slotHandle.handle(wrapper);
          wrapper.copyFloat();
          wrapper.copyVarInt();
          break;
        }
        case "minecraft:stonecutting": {
          wrapper.copyVarString();
          handleIngredient(wrapper);
          slotHandle.handle(wrapper);
          break;
        }
        case "minecraft:smithing_transform": {
          handleIngredient(wrapper);
          handleIngredient(wrapper);
          handleIngredient(wrapper);
          slotHandle.handle(wrapper);
          break;
        }
        case "minecraft:smithing_trim": {
          handleIngredient(wrapper);
          handleIngredient(wrapper);
          handleIngredient(wrapper);
          break;
        }
      }
    }
  }

  public void handleIngredient(final @NotNull Wrapper wrapper) throws IOException {
    int count = wrapper.copyVarInt();
    for (int i = 0; i < count; i++) {
      slotHandle.handle(wrapper);
    }
  }
}
