package de.mathit.imagetool.attribute.meta;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategy that reads metadata from a mp4. Of course, no index can be provided.
 */
public class Mp4AttributeStrategy extends MetadataAttributeStrategySupport {

  public Mp4AttributeStrategy(final File path) {
    super(path);
  }

  @Override
  String getExtension() {
    return "mp4";
  }

  @Override
  protected LocalDateTime getCreationDateTime(final File path)
      throws ImageProcessingException, IOException {
    final Metadata metadata = Mp4MetadataReader.readMetadata(path);
    final String datetime = getString(metadata.getDirectoriesOfType(Mp4Directory.class),
        Mp4Directory.TAG_CREATION_TIME);
    if (datetime != null) {
      final String[] tokens = datetime.split(" ");
      final Map<String, String> monthMap = new HashMap<>();
      monthMap.put("Mar", "03");

      if (!monthMap.containsKey(tokens[1])) {
        throw new IllegalStateException("No entry yet for month '" + tokens[1] + "'");
      }
      final String month = monthMap.get(tokens[1]);
      final String datetimeModified = tokens[5] + ":" + month + ":" + tokens[2] + " " + tokens[3];
      return parse(datetimeModified, "yyyy:MM:dd HH:mm:ss");
    }
    return null;
  }

}