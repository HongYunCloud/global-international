package io.github.hongyuncloud.gi;

import com.ibm.icu.util.ULocale;
import io.github.hongyuncloud.gi.format.TranslateFormat;
import io.github.hongyuncloud.gi.source.TranslateSource;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class GiCore {
  private static final @NotNull Logger logger = LoggerFactory.getLogger(GiCore.class);

  private final @NotNull Object updateLock = new Object();
  private final @NotNull List<TranslateSource> sources = new ArrayList<>();
  private final @NotNull Map<@NotNull String, @NotNull Map<@NotNull String, @NotNull Optional<@NotNull TranslateFormat>>> map = new HashMap<>();

  public GiCore(final @NotNull List<TranslateSource> sources) {
    this.sources.addAll(sources);
  }

  public GiCore(final @NotNull TranslateSource @NotNull ... sources) {
    this(Arrays.asList(sources));
  }

  public @Nullable TranslateFormat getFormat(final @NotNull String rawLocal, final @NotNull String rawKey) {
    String locale = rawLocal.toLowerCase(Locale.ROOT);
    String key = rawKey.toLowerCase(Locale.ROOT);

    Map<String, Optional<TranslateFormat>> formatMap = map.get(locale);
    if (formatMap == null) {
      synchronized (updateLock) {
        formatMap = map.computeIfAbsent(locale, it -> new ConcurrentHashMap<>());
      }
    }

    Optional<@NotNull TranslateFormat> formatOptional = formatMap.computeIfAbsent(key, it -> {
      for (TranslateSource source : sources) {
        TranslateFormat format = source.provide(locale, key);
        if (format != null) {
          return Optional.of(format);
        }
      }
      return Optional.empty();
    });

    return formatOptional.orElse(null);
  }

  public @NotNull String text(final @NotNull GiUserConfig context, final @NotNull String text) {
    logger.debug("translate text: {}", text);
    return text;
  }

  public @NotNull String component(final @NotNull GiUserConfig context, final @NotNull String componentString) {
    logger.debug("translate component: {}", componentString);

    Component sourceComponent = JSONComponentSerializer.json()
        .deserialize(componentString);

    String result;
    if (sourceComponent instanceof BuildableComponent) {
      Component component = componentImpl(context, (BuildableComponent) sourceComponent);
      result = JSONComponentSerializer.json().serialize(component);
    } else {
      result = componentString;
    }

    logger.debug("translate component: {}\n{}", componentString, result);

    return result;
  }

  private void translatableComponentImpl(
      final @NotNull GiUserConfig context,
      final @NotNull TranslatableComponent component,
      final @NotNull TranslatableComponent.@NotNull Builder builder) {
    TranslateFormat format = null;
    for (ULocale locale : context.localePriorityList()) {
      format = getFormat(locale.getName(), component.key());
      if (format != null) {
        break;
      }
    }

    if (format != null) {
      builder.key(component.key() + "!");
      format.applyTranslate(context, component, builder);
    }

    List<TranslationArgument> mappedArguments = component.arguments()
        .stream()
        .map(it -> {
          Object value = it.value();
          if (value instanceof Number) {
            String formatted = context.numberFormat()
                .format((Number) value)
                .toString();
            return TranslationArgument.component(Component.text(formatted));
          } else if (value instanceof Boolean) {
            return TranslationArgument.component(Component.text(value.toString()));
          } else if (value instanceof BuildableComponent) {
            return TranslationArgument.component(componentImpl(context, (BuildableComponent<?, ?>) value));
          } else {
            return it;
          }
        }).collect(Collectors.toList());

    builder.arguments(mappedArguments);
  }

  private @NotNull <C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> C componentImpl(
      final @NotNull GiUserConfig context,
      final @NotNull BuildableComponent<C, B> component) {
    B builder = component.toBuilder();
    if (component instanceof TranslatableComponent) {
      TranslatableComponent translatableComponent = (TranslatableComponent) component;
      TranslatableComponent.Builder translatableComponentBuilder = (TranslatableComponent.Builder) builder;
      translatableComponentImpl(context, translatableComponent, translatableComponentBuilder);
    }
    if (component.hoverEvent() != null) {
      HoverEvent.Action<?> action = component.hoverEvent().action();
      Object value = component.hoverEvent().value();
      if (action == HoverEvent.Action.SHOW_TEXT && value instanceof BuildableComponent) {
        builder.hoverEvent(componentImpl(context, (BuildableComponent<?, ?>) value));
      } else if (action == HoverEvent.Action.SHOW_ENTITY) {
        builder.hoverEvent(null);
      }
    }
    builder.mapChildren(child -> componentImpl(context, child));
    return builder.build();
  }
}
