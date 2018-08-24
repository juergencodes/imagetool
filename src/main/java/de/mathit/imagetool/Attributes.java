package de.mathit.imagetool;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Collects attributes of an image.
 */
public class Attributes {

  private final Path path;
  private LocalDate day;
  private LocalTime time;
  private String index;

  public Attributes(final Path path) {
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  public LocalDate getDay() {
    return day;
  }

  public void setDay(final LocalDate day) {
    if (this.day != null) {
      return;
    }
    this.day = day;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(final LocalTime time) {
    if (this.time != null) {
      return;
    }
    this.time = time;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(final String index) {
    if (this.index != null) {
      return;
    }
    this.index = index;
  }

  @Override
  public String toString() {
    return "Attributes{" + "path=" + path + ", day=" + day + ", time=" + time +
        ", index='" + index + '\'' + '}';
  }

}