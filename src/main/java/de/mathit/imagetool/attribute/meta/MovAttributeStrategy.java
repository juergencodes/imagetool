package de.mathit.imagetool.attribute.meta;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Strategy that reads metadata from a mov. Of course, no index can be provided.
 */
public class MovAttributeStrategy extends MetadataAttributeStrategySupport {

  public MovAttributeStrategy(final Path path) {
    super(path);
  }

  @Override
  String getExtension() {
    return "mov";
  }

  @Override
  protected LocalDateTime getCreationDateTime(final File file)
      throws ImageProcessingException, IOException {
    final Metadata metadata = QuickTimeMetadataReader.readMetadata(file);

    String datetime = getString(metadata.getDirectoriesOfType(QuickTimeMetadataDirectory.class),
        0x0506);
    if (datetime != null) {
      if (datetime.length() == 24) {
        datetime = datetime.substring(0, 19);
      }
      return parse(datetime, "yyyy-MM-dd'T'HH:mm:ss");
    }
    return null;
  }

}