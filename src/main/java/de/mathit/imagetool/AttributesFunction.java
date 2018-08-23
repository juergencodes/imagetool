package de.mathit.imagetool;

import static de.mathit.imagetool.MetadataSupport.getString;
import static de.mathit.imagetool.MetadataSupport.parse;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Get the attributes of an image given by path.
 */
public class AttributesFunction implements Function<Path, Attributes> {

  /**
   * Probably the most accurate strategy for index and day, because it takes day and index from the
   * filename with the target format name. However, the time is not available.
   */
  private static final Pattern PATTERN_TARGET = Pattern
      .compile("(.*[ ][\\[]([0-9]{4}[-][0-9]{2}[-][0-9]{2})[\\]][ ][-][ ])(.*)([.].{3})");

  /**
   * Parse the filename provided by Galaxy smartphones, because it is assumed that the date and time
   * written to the filename is in doubt more precise than the EXIF tags. Example:
   * 20180406_102615.jpg
   */
  private static final Pattern PATTERN_GALAXY = Pattern
      .compile("([0-9]{8})[_]([0-9]{6}).*[.].{3}");

  private List<BiConsumer<File, Attributes>> strategies = new LinkedList<>();

  public AttributesFunction() {
    // Target name
    strategies.add((f, a) -> {
      final Matcher matcher = PATTERN_TARGET.matcher(f.getName());
      if (matcher.matches()) {
        a.setDay(LocalDate.parse(matcher.group(2)));
        a.setIndex(matcher.group(3));
      }
    });

    // Galaxy filename
    strategies.add((f, a) -> {
      final Matcher matcher = PATTERN_GALAXY.matcher(f.getName());
      if (matcher.matches()) {
        a.setDay(LocalDate.parse(matcher.group(1), DateTimeFormatter.ofPattern("yyyyMMdd")));
        a.setTime(LocalTime.parse(matcher.group(2), DateTimeFormatter.ofPattern("HHmmss")));
      }
    });

    // JPG Metadata
    strategies.add(new MetadataSupport("jpg", f -> {
      final Metadata metadata = JpegMetadataReader
          .readMetadata(f, Arrays.asList(new ExifReader()));

      // getString(metadata.getDirectoriesOfType(ExifIFD0Directory.class), ExifDirectoryBase.TAG_MODEL);
      final String datetime = getString(metadata.getDirectoriesOfType(ExifSubIFDDirectory.class),
          ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
      return parse(datetime, "yyyy:MM:dd HH:mm:ss");
    }));

    // MOV Metadata
    strategies.add(new MetadataSupport("mov", f -> {
      final Metadata metadata = QuickTimeMetadataReader.readMetadata(f);

      String datetime = getString(metadata.getDirectoriesOfType(QuickTimeMetadataDirectory.class),
          0x0506);
      if (datetime != null) {
        if (datetime.length() == 24) {
          datetime = datetime.substring(0, 19);
        }
        return parse(datetime, "yyyy-MM-dd'T'HH:mm:ss");
      }
      return null;
    }));

    // MP4 Metadata
    strategies.add(new MetadataSupport("mp4", f -> {
      final Metadata metadata = Mp4MetadataReader.readMetadata(f);
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
    }));
  }

  @Override
  public Attributes apply(final Path path) {
    final Attributes attributes = new Attributes(path);
    final File file = path == null ? null : path.toFile();
    for (final BiConsumer<File, Attributes> consumer : strategies) {
      consumer.accept(file, attributes);
    }
    return attributes;
  }

}