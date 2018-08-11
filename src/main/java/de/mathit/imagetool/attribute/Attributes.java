package de.mathit.imagetool.attribute;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Collects attributes of an image.
 */
public class Attributes {

  private final Path path;
  private final LocalDate day;
  private final LocalTime time;
  private final String index;

  Attributes(final Path path, final LocalDate day, final LocalTime time, final String index) {
    this.path = path;
    this.day = day;
    this.time = time;
    this.index = index;
  }

  public Path getPath() {
    return path;
  }

  public LocalDate getDay() {
    return day;
  }

  public LocalTime getTime() {
    return time;
  }

  public String getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "Attributes{" + "path=" + path + ", day=" + day + ", time=" + time +
        ", index='" + index + '\'' + '}';
  }

}