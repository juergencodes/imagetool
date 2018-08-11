package de.mathit.imagetool.attribute.meta;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Strategy that reads EXIF metadata from a jpg. Of course, no index can be provided.
 */
public class JpgAttributeStrategy extends MetadataAttributeStrategySupport {

  public JpgAttributeStrategy(final File path) {
    super(path);
  }

  @Override
  String getExtension() {
    return "jpg";
  }

  @Override
  protected LocalDateTime getCreationDateTime(final File path)
      throws JpegProcessingException, IOException {
    final Metadata metadata = JpegMetadataReader
        .readMetadata(path, Arrays.asList(new ExifReader()));

    // getString(metadata.getDirectoriesOfType(ExifIFD0Directory.class), ExifDirectoryBase.TAG_MODEL);
    final String datetime = getString(metadata.getDirectoriesOfType(ExifSubIFDDirectory.class),
        ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
    return parse(datetime, "yyyy:MM:dd HH:mm:ss");
  }

}