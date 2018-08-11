package de.mathit.imagetool.attribute.file;

import de.mathit.imagetool.attribute.AttributeStrategy;
import java.io.File;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Probably the most accurate strategy for index and day, because it takes day and index from the
 * filename with the target format name. However, the time is not available.
 */
public class TargetFileNameAttributeStrategy extends AttributeStrategy {

  private static final Pattern PATTERN = Pattern
      .compile("(.*[ ][\\[]([0-9]{4}[-][0-9]{2}[-][0-9]{2})[\\]][ ][-][ ])(.*)([.].{3})");

  public TargetFileNameAttributeStrategy(final File path) {
    super(path);
  }

  @Override
  protected void init(final File path) {
    final Matcher matcher = PATTERN.matcher(path.getName());
    if (matcher.matches()) {
      registerDay(LocalDate.parse(matcher.group(2)));
      registerIndex(matcher.group(3));
    }
  }

}