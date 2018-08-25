package de.mathit.imagetool;

import static java.time.format.DateTimeFormatter.ofPattern;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Get the attributes of an image given by path.
 */
public class AttributesFunction implements Function<File, Attributes> {

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
        a.setDay(LocalDate.parse(matcher.group(1), ofPattern("yyyyMMdd")));
        a.setTime(LocalTime.parse(matcher.group(2), ofPattern("HHmmss")));
      }
    });

    // JPG Metadata
    strategies.add(metadata("jpg", (f, a) -> JpegMetadataReader
        .readMetadata(f, Arrays.asList(new ExifReader()))
        .getDirectoriesOfType(ExifSubIFDDirectory.class)
        .stream().findFirst().map(d -> d.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL))
        .map(d -> LocalDateTime.parse(d, ofPattern("yyyy:MM:dd HH:mm:ss")))
        .ifPresent(a)));

    // MOV Metadata
    strategies.add(metadata("mov", (f, a) -> QuickTimeMetadataReader.readMetadata(f)
        .getDirectoriesOfType(QuickTimeMetadataDirectory.class).stream()
        .findFirst().map(d -> d.getString(0x0506))
        .map(d -> d.length() == 24 ? d.substring(0, 19) : d)
        .map(d -> LocalDateTime.parse(d, ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).ifPresent(a)));

    // MP4 Metadata
    strategies.add(metadata("mp4", (f, a) -> Mp4MetadataReader.readMetadata(f)
        .getDirectoriesOfType(Mp4Directory.class).stream().findFirst()
        .map(d -> d.getString(Mp4Directory.TAG_CREATION_TIME)).map(d -> d.split(" "))
        .map(t -> t[5] + ":" + t[1] + ":" + t[2] + " " + t[3])
        .map(d -> LocalDateTime.parse(d, ofPattern("yyyy:MMM:dd HH:mm:ss"))).ifPresent(a)));
  }

  private BiConsumer<File, Attributes> metadata(final String extension,
      final MetadataBiConsumer consumer) {
    return (f, a) -> {
      if (f.getPath().toLowerCase().endsWith(extension) && f.exists()) {
        try {
          consumer.accept(f, a);
        } catch (final ImageProcessingException | IOException e) {
          System.err.println("Cannot read file: " + e.getMessage());
        }
      }
    };
  }

  @Override
  public Attributes apply(final File file) {
    final Attributes attributes = new Attributes(file);
    Optional.of(file)
        .ifPresent(f -> strategies.stream().forEach(s -> s.accept(f, attributes)));
    return attributes;
  }

  private interface MetadataBiConsumer {

    void accept(File file, Attributes attributes) throws IOException, ImageProcessingException;

  }

}