package de.mathit.imagetool;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
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
import java.util.LinkedList;
import java.util.List;
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
    strategies.add(new MetadataSupport("jpg", "yyyy:MM:dd HH:mm:ss", f -> JpegMetadataReader
        .readMetadata(f, Arrays.asList(new ExifReader()))
        .getDirectoriesOfType(ExifSubIFDDirectory.class)
        .stream().findFirst().map(d -> d.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL))
        .orElse(null)));

    // MOV Metadata
    strategies.add(new MetadataSupport("mov", "yyyy-MM-dd'T'HH:mm:ss",
        f -> QuickTimeMetadataReader.readMetadata(f)
            .getDirectoriesOfType(QuickTimeMetadataDirectory.class).stream()
            .findFirst().map(d -> d.getString(0x0506))
            .map(d -> d.length() == 24 ? d.substring(0, 19) : d).orElse(null)));

    // MP4 Metadata
    strategies.add(
        new MetadataSupport("mp4", "yyyy:MMM:dd HH:mm:ss", f -> Mp4MetadataReader.readMetadata(f)
            .getDirectoriesOfType(Mp4Directory.class).stream().findFirst()
            .map(d -> d.getString(Mp4Directory.TAG_CREATION_TIME)).map(d -> d.split(" "))
            .map(t -> t[5] + ":" + t[1] + ":" + t[2] + " " + t[3]).orElse(null)));
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