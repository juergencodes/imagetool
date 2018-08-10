package de.mathit.imagetool.image;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Strategy to get the relevant information from an image's path. An implementation of this
 * interface may apply different algorithms like parse filename or extract metadata. Thereby, every
 * implementation should be as accurate as possible, hence having the freedom to not return a result
 * at all.
 */
public abstract class ImageStrategy {

  private Optional<LocalDate> day = Optional.empty();
  private Optional<LocalTime> time = Optional.empty();
  private Optional<String> index = Optional.empty();
  private boolean initialized = false;

  public ImageStrategy(final File path) {
    init(path);
  }

  protected void registerDay(final LocalDate day) {
    this.day = Optional.of(day);
  }

  protected void registerTime(final LocalTime time) {
    this.time = Optional.of(time);
  }

  protected void registerIndex(final String index) {
    this.index = Optional.of(index);
  }

  protected abstract void init(final File path);

  public static LocalDate day(final ImageStrategy... strategies) {
    return firstProvided(s -> s.day, strategies);
  }

  public static LocalTime time(final ImageStrategy... strategies) {
    return firstProvided(s -> s.time, strategies);
  }

  public static String index(final ImageStrategy... strategies) {
    return firstProvided(s -> s.index, strategies);
  }

  private static <T> T firstProvided(final Function<ImageStrategy, Optional<T>> function,
      final ImageStrategy... strategies) {
    return Stream.of(strategies).map(function)
        .reduce(Optional.empty(), (s, t) -> s.isPresent() ? s : t).orElse(null);
  }

}