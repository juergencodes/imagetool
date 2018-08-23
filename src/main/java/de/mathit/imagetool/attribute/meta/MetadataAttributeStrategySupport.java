package de.mathit.imagetool.attribute.meta;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import de.mathit.imagetool.attribute.AttributeStrategy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

/**
 * Support class to simplify implementations that read metadata.
 */
abstract class MetadataAttributeStrategySupport extends AttributeStrategy {

  public MetadataAttributeStrategySupport(final Path path) {
    super(path);
  }

  @Override
  protected void init(final File file) {
    if (file.getPath().toLowerCase().endsWith(getExtension()) && file.exists()) {
      try {
        final LocalDateTime creationDateTime = getCreationDateTime(file);
        if (creationDateTime != null) {
          registerDay(creationDateTime.toLocalDate());
          registerTime(creationDateTime.toLocalTime());
        }
      } catch (final ImageProcessingException | IOException e) {
        System.err.println("Cannot read file: " + e.getMessage());
      }
    }
  }

  abstract String getExtension();

  abstract LocalDateTime getCreationDateTime(final File file)
      throws IOException, ImageProcessingException;

  <E extends Directory> String getString(final Collection<E> directories,
      final int tagType) {
    String result;
    final Iterator<E> iterator = directories.iterator();
    if (iterator.hasNext()) {
      result = iterator.next().getString(tagType);
    } else {
      return null;
    }
    if (iterator.hasNext()) {
      // Probably ok to have more directories or are we picking the wrong data?
    }
    return result;
  }

  LocalDateTime parse(final String datetime, final String format) {
    return datetime == null || "".equals(datetime) ? null
        : LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(format));
  }

}