package de.mathit.imagetool.attribute.file;

import de.mathit.imagetool.attribute.AttributeStrategy;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse the filename provided by Galaxy smartphones, because it is assumed that the date and time
 * written to the filename is in doubt more precise than the EXIF tags. Example: 20180406_102615.jpg
 */
public class GalaxyFileNameAttributeStrategy extends AttributeStrategy {

  private static final Pattern PATTERN = Pattern
      .compile("([0-9]{8})[_]([0-9]{6}).*[.].{3}");

  public GalaxyFileNameAttributeStrategy(final File path) {
    super(path);
  }

  @Override
  protected void init(final File path) {
    final Matcher matcher = PATTERN.matcher(path.getName());
    if (matcher.matches()) {
      registerDay(LocalDate.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyyMMdd")));
      registerTime(LocalTime.parse(matcher.group(2), DateTimeFormatter.ofPattern("HHmmss")));
    }
  }

}