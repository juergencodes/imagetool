package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;

/**
 * Support class to simplify implementations that read metadata.
 */
public class MetadataSupport implements BiConsumer<File, Attributes> {

  private final String extension;
  private final Extractor extractor;

  public MetadataSupport(final String extension, final Extractor extractor) {
    this.extension = extension;
    this.extractor = extractor;
  }

  @Override
  public void accept(final File file, final Attributes attributes) {
    if (file.getPath().toLowerCase().endsWith(extension) && file.exists()) {
      try {
        final LocalDateTime creationDateTime = extractor.extract(file);
        if (creationDateTime != null) {
          attributes.setDay(creationDateTime.toLocalDate());
          attributes.setTime(creationDateTime.toLocalTime());
        }
      } catch (final ImageProcessingException | IOException e) {
        System.err.println("Cannot read file: " + e.getMessage());
      }
    }
  }

  static <E extends Directory> String getString(final Collection<E> directories,
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

  static LocalDateTime parse(final String datetime, final String format) {
    return datetime == null || "".equals(datetime) ? null
        : LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(format));
  }

  public interface Extractor {

    LocalDateTime extract(File file) throws IOException, ImageProcessingException;

  }

}