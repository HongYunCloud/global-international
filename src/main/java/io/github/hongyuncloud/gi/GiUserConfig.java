package io.github.hongyuncloud.gi;

import com.google.common.collect.Lists;
import com.ibm.icu.number.LocalizedNumberFormatter;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.RelativeDateTimeFormatter;
import com.ibm.icu.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class GiUserConfig {
  private static final @NotNull GiUserConfig DEFAULT = GiUserConfig.builder().build();

  private final boolean locale$set;
  private final @NotNull ULocale locale;

  private final boolean localePriorityList$set;
  private final @NotNull LocalePriorityList localePriorityList;

  private final boolean region$set;
  private final @NotNull Region region;

  private final boolean numberFormatter$set;
  private final @NotNull LocalizedNumberFormatter numberFormatter;

  private final boolean timeZone$set;
  private final @NotNull TimeZone timeZone;

  private final boolean calendar$set;
  private final @NotNull Calendar calendar;

  private final boolean dateFormat$set;
  private final @NotNull DateFormat dateFormat;

  private final boolean dateTimeFormat$set;
  private final @NotNull DateFormat dateTimeFormat;

  private final boolean relativeDateTimeFormatter$set;
  private final @NotNull RelativeDateTimeFormatter relativeDateTimeFormatter;

  public GiUserConfig(
      final boolean locale$set, final @NotNull ULocale locale,
      final boolean localePriorityList$set, final @NotNull LocalePriorityList localePriorityList,
      final boolean region$set, final @NotNull Region region,
      final boolean numberFormatter$set, final @NotNull LocalizedNumberFormatter numberFormatter,
      final boolean timeZone$set, final @NotNull TimeZone timeZone,
      final boolean calendar$set, final @NotNull Calendar calendar,
      final boolean dateFormat$set, final @NotNull DateFormat dateFormat,
      final boolean dateTimeFormat$set, final @NotNull DateFormat dateTimeFormat,
      final boolean relativeDateTimeFormatter$set, final @NotNull RelativeDateTimeFormatter relativeDateTimeFormatter
  ) {
    this.locale$set = locale$set;
    this.locale = locale;

    this.localePriorityList$set = localePriorityList$set;
    this.localePriorityList = localePriorityList;

    this.region$set = region$set;
    this.region = region;

    this.numberFormatter$set = numberFormatter$set;
    this.numberFormatter = numberFormatter;

    this.timeZone$set = timeZone$set;
    this.timeZone = timeZone;

    this.calendar$set = calendar$set;
    this.calendar = calendar;

    this.dateFormat$set = dateFormat$set;
    this.dateFormat = dateFormat;

    this.dateTimeFormat$set = dateTimeFormat$set;
    this.dateTimeFormat = dateTimeFormat;

    this.relativeDateTimeFormatter$set = relativeDateTimeFormatter$set;
    this.relativeDateTimeFormatter = relativeDateTimeFormatter;
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }

  public static @NotNull GiUserConfig empty() {
    return DEFAULT;
  }

  public @NotNull ULocale locale() {
    return locale;
  }

  public @NotNull LocalePriorityList localePriorityList() {
    return localePriorityList;
  }

  public @NotNull Region region() {
    return region;
  }

  public @NotNull LocalizedNumberFormatter numberFormat() {
    return numberFormatter;
  }

  public @NotNull TimeZone timeZone() {
    return timeZone;
  }

  public @NotNull Calendar calendar() {
    return calendar;
  }

  public @NotNull DateFormat dateFormat() {
    return dateFormat;
  }

  public @NotNull DateFormat dateTimeFormat() {
    return dateTimeFormat;
  }

  public @NotNull RelativeDateTimeFormatter relativeDateTimeFormatter() {
    return relativeDateTimeFormatter;
  }

  public static final class Builder {
    private final @NotNull List<GiUserConfig> parents = new ArrayList<>();

    private boolean locale$set;
    private @Nullable ULocale locale;

    private boolean localePriorityList$set;
    private @Nullable LocalePriorityList localePriorityList;

    private boolean region$set;
    private @Nullable Region region;

    private boolean numberFormatter$set;
    private @Nullable LocalizedNumberFormatter numberFormatter;

    private boolean timeZone$set;
    private @Nullable TimeZone timeZone;

    private boolean calendar$set;
    private @Nullable Calendar calendar;

    private boolean dateFormat$set;
    private @Nullable DateFormat dateFormat;

    private boolean dateTimeFormat$set;
    private @Nullable DateFormat dateTimeFormat;

    private boolean relativeDateTimeFormatter$set;
    private @Nullable RelativeDateTimeFormatter relativeDateTimeFormatter;

    public @NotNull Builder addParent(final @NotNull GiUserConfig parent) {
      this.parents.add(parent);
      return this;
    }

    public @NotNull Builder addParents(final @NotNull Collection<? extends GiUserConfig> parents) {
      this.parents.addAll(parents);
      return this;
    }

    public @NotNull Builder addParents(final @NotNull GiUserConfig @NotNull [] parents) {
      this.parents.addAll(Arrays.asList(parents));
      return this;
    }

    public @NotNull Builder setLocale(final @Nullable ULocale locale) {
      this.locale$set = true;
      this.locale = locale;
      return this;
    }

    public @NotNull Builder clearLocale() {
      this.locale$set = false;
      this.locale = null;
      return this;
    }

    public @NotNull Builder setLocalePriorityList(final @Nullable LocalePriorityList localePriorityList) {
      this.localePriorityList$set = true;
      this.localePriorityList = localePriorityList;
      return this;
    }

    public @NotNull Builder clearLocalePriorityList() {
      this.localePriorityList$set = false;
      this.localePriorityList = null;
      return this;
    }

    public @NotNull Builder setRegion(final @Nullable Region region) {
      this.region$set = true;
      this.region = region;
      return this;
    }

    public @NotNull Builder clearRegion() {
      this.region$set = false;
      this.region = null;
      return this;
    }

    public @NotNull Builder setNumberFormatter(final @Nullable LocalizedNumberFormatter numberFormatter) {
      this.numberFormatter$set = true;
      this.numberFormatter = numberFormatter;
      return this;
    }

    public @NotNull Builder clearNumberFormatter() {
      this.numberFormatter$set = false;
      this.numberFormatter = null;
      return this;
    }

    public @NotNull Builder setTimeZone(final @Nullable TimeZone timeZone) {
      this.timeZone$set = true;
      this.timeZone = timeZone;
      return this;
    }

    public @NotNull Builder clearTimeZone() {
      this.timeZone$set = false;
      this.timeZone = null;
      return this;
    }

    public @NotNull Builder setCalendar(final @Nullable Calendar calendar) {
      this.calendar$set = true;
      this.calendar = calendar;
      return this;
    }

    public @NotNull Builder clearCalendar() {
      this.calendar$set = false;
      this.calendar = null;
      return this;
    }

    public @NotNull Builder setDateFormat(final @Nullable DateFormat dateFormat) {
      this.dateFormat$set = true;
      this.dateFormat = dateFormat;
      return this;
    }

    public @NotNull Builder clearDateFormat() {
      this.dateFormat$set = false;
      this.dateFormat = null;
      return this;
    }

    public @NotNull Builder setDateTimeFormat(final @Nullable DateFormat dateTimeFormat) {
      this.dateTimeFormat$set = true;
      this.dateTimeFormat = dateTimeFormat;
      return this;
    }

    public @NotNull Builder clearDateTimeFormat() {
      this.dateTimeFormat$set = false;
      this.dateTimeFormat = null;
      return this;
    }

    public @NotNull Builder setRelativeDateTimeFormatter(final @Nullable RelativeDateTimeFormatter relativeDateTimeFormatter) {
      this.relativeDateTimeFormatter$set = true;
      this.relativeDateTimeFormatter = relativeDateTimeFormatter;
      return this;
    }

    public @NotNull Builder clearRelativeDateTimeFormatter() {
      this.relativeDateTimeFormatter$set = false;
      this.relativeDateTimeFormatter = null;
      return this;
    }

    public @NotNull GiUserConfig build() {
      List<GiUserConfig> parents = Lists.reverse(this.parents);

      boolean locale$set = this.locale$set;
      ULocale locale = this.locale;
      if (!locale$set) {
        for (GiUserConfig parent : parents) {
          if (parent.locale$set) {
            locale$set = true;
            locale = parent.locale;
            break;
          }
        }
      }
      if (!locale$set || locale == null) {
        locale = ULocale.CHINA;
      }

      boolean localePriorityList$set = this.localePriorityList$set;
      LocalePriorityList localePriorityList = this.localePriorityList;
      if (!localePriorityList$set) {
        for (GiUserConfig parent : parents) {
          if (parent.localePriorityList$set) {
            localePriorityList$set = true;
            localePriorityList = parent.localePriorityList;
            break;
          }
        }
      }
      if (!localePriorityList$set || localePriorityList == null) {
        LocalePriorityList.Builder builder = LocalePriorityList.add(locale);
        if (!locale.getScript().isEmpty()) {
          builder.add(new ULocale.Builder()
              .setLocale(locale)
              .setScript("")
              .build());
        }
        builder.add(ULocale.ROOT);
        localePriorityList = builder.build();
      }

      boolean region$set = this.region$set;
      Region region = this.region;
      if (!region$set) {
        for (GiUserConfig parent : parents) {
          if (parent.region$set) {
            region$set = true;
            region = parent.region;
            break;
          }
        }
      }
      if (!region$set || region == null) {
        region = Region.getInstance(locale.getCountry());
      }

      boolean numberFormatter$set = this.numberFormatter$set;
      LocalizedNumberFormatter numberFormatter = this.numberFormatter;
      if (!numberFormatter$set) {
        for (GiUserConfig parent : parents) {
          if (parent.numberFormatter$set) {
            numberFormatter$set = true;
            numberFormatter = parent.numberFormatter;
            break;
          }
        }
      }
      if (!numberFormatter$set || numberFormatter == null) {
        numberFormatter = NumberFormatter.withLocale(locale);
      }

      boolean timeZone$set = this.timeZone$set;
      TimeZone timeZone = this.timeZone;
      if (!timeZone$set) {
        for (GiUserConfig parent : parents) {
          if (parent.timeZone$set) {
            timeZone$set = true;
            timeZone = parent.timeZone;
            break;
          }
        }
      }
      if (!timeZone$set || timeZone == null) {
        timeZone = TimeZone.forULocaleOrDefault(locale);
      }

      boolean calendar$set = this.calendar$set;
      Calendar calendar = this.calendar;
      if (!calendar$set) {
        for (GiUserConfig parent : parents) {
          if (parent.calendar$set) {
            calendar$set = true;
            calendar = parent.calendar;
            break;
          }
        }
      }
      if (!calendar$set || calendar == null) {
        calendar = Calendar.getInstance(timeZone, locale);
      }

      boolean dateFormat$set = this.dateFormat$set;
      DateFormat dateFormat = this.dateFormat;
      if (!dateFormat$set) {
        for (GiUserConfig parent : parents) {
          if (parent.dateFormat$set) {
            dateFormat$set = true;
            dateFormat = parent.dateFormat;
            break;
          }
        }
      }
      if (!dateFormat$set || dateFormat == null) {
        dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
      }

      boolean dateTimeFormat$set = this.dateTimeFormat$set;
      DateFormat dateTimeFormat = this.dateTimeFormat;
      if (!dateTimeFormat$set) {
        for (GiUserConfig parent : parents) {
          if (parent.dateTimeFormat$set) {
            dateTimeFormat$set = true;
            dateTimeFormat = parent.dateTimeFormat;
            break;
          }
        }
      }
      if (!dateTimeFormat$set || dateTimeFormat == null) {
        dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
      }

      boolean relativeDateTimeFormatter$set = this.relativeDateTimeFormatter$set;
      RelativeDateTimeFormatter relativeDateTimeFormatter = this.relativeDateTimeFormatter;
      if (!relativeDateTimeFormatter$set) {
        for (GiUserConfig parent : parents) {
          if (parent.relativeDateTimeFormatter$set) {
            relativeDateTimeFormatter$set = true;
            relativeDateTimeFormatter = parent.relativeDateTimeFormatter;
            break;
          }
        }
      }
      if (!relativeDateTimeFormatter$set || relativeDateTimeFormatter == null) {
        relativeDateTimeFormatter = RelativeDateTimeFormatter.getInstance(locale);
      }

      return new GiUserConfig(
          locale$set, locale,
          localePriorityList$set, localePriorityList,
          region$set, region,
          numberFormatter$set, numberFormatter,
          timeZone$set, timeZone,
          calendar$set, calendar,
          dateFormat$set, dateFormat,
          dateTimeFormat$set, dateTimeFormat,
          relativeDateTimeFormatter$set, relativeDateTimeFormatter);
    }
  }
}
