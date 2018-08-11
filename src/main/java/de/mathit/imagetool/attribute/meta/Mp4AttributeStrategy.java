package de.mathit.imagetool.attribute.meta;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Strategy that reads metadata from a mp4. Of course, no index can be provided.
 */
public class Mp4AttributeStrategy extends MetadataAttributeStrategySupport {

  public Mp4AttributeStrategy(final Path path) {
    super(path);
  }

  @Override
  String getExtension() {
    return "mp4";
  }

  @Override
  protected LocalDateTime getCreationDateTime(final File file)
      throws ImageProcessingException, IOException {
    final Metadata metadata = Mp4MetadataReader.readMetadata(file);
    final String datetime = getString(metadata.getDirectoriesOfType(Mp4Directory.class),
        Mp4Directory.TAG_CREATION_TIME);
    if (datetime != null) {
      final String[] tokens = datetime.split(" ");
      final Map<String, String> monthMap = new HashMap<>();
      monthMap.put("Jan", "01");
      monthMap.put("Feb", "02");
      monthMap.put("Mar", "03");
      monthMap.put("Apr", "04");
      monthMap.put("May", "05");
      monthMap.put("Jun", "06");
      monthMap.put("Jul", "07");
      monthMap.put("Aug", "08");
      monthMap.put("Sep", "09");
      monthMap.put("Oct", "10");
      monthMap.put("Nov", "11");
      monthMap.put("Dev", "12");

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