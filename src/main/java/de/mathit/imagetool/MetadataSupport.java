package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

/**
 * Support class to simplify implementations that read metadata.
 */
public class MetadataSupport implements BiConsumer<File, Attributes> {

  private final String extension;
  private final String pattern;
  private final Extractor extractor;

  public MetadataSupport(final String extension, final String pattern, final Extractor extractor) {
    this.extension = extension;
    this.pattern = pattern;
    this.extractor = extractor;
  }

  @Override
  public void accept(final File file, final Attributes attributes) {
    if (file.getPath().toLowerCase().endsWith(extension) && file.exists()) {
      try {
        final String datetime = extractor.extract(file);
        if (datetime != null && !"".equals(datetime)) {
          final LocalDateTime creationDateTime = LocalDateTime
              .parse(datetime, DateTimeFormatter.ofPattern(pattern));
          attributes.setDay(creationDateTime.toLocalDate());
          attributes.setTime(creationDateTime.toLocalTime());
        }
      } catch (final ImageProcessingException | IOException e) {
        System.err.println("Cannot read file: " + e.getMessage());
      }
    }
  }

  public interface Extractor {

    String extract(File file) throws IOException, ImageProcessingException;

  }

}