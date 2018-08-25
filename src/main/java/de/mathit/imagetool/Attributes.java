package de.mathit.imagetool;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Consumer;

/**
 * Collects attributes of an image.
 */
public class Attributes implements Consumer<LocalDateTime> {

  private final File file;
  private LocalDate day;
  private LocalTime time;
  private String index;

  public Attributes(final File file) {
    this.file = file;
  }

  public File getFile() {
    return file;
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
  public void accept(final LocalDateTime creationDateTime) {
    setDay(creationDateTime.toLocalDate());
    setTime(creationDateTime.toLocalTime());
  }

  @Override
  public String toString() {
    return "Attributes{" + "file=" + file + ", day=" + day + ", time=" + time +
        ", index='" + index + '\'' + '}';
  }

}