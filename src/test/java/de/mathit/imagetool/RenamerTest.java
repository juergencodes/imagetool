package de.mathit.imagetool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.mathit.imagetool.attribute.Attributes;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link Renamer}
 */
public class RenamerTest {

  private final String ALBUM = "MyAlbum";
  private final static LocalDate DAY = LocalDate.of(2018, 8, 15);
  private final static LocalTime TIME = LocalTime.of(13, 52);
  private int counter;

  @Before
  public void init() {
    counter = 1;
  }

  @Test
  public void singleRename() {
    final Attributes attributes = attributes(DAY, TIME, null);

    final Map<Attributes, String> renames = renames(attributes);
    assertNumberOfRenames(1, renames);
    assertRenameTo(renames, attributes, "MyAlbum [2018-08-15] - 001.jpg");
  }

  @Test
  public void twoRenamesWithExistingIndex() {
    final Attributes attributes1 = attributes(DAY, TIME, "001");
    final Attributes attributes2 = attributes(DAY, TIME, "002");

    final Map<Attributes, String> renames = renames(attributes1, attributes2);
    assertNumberOfRenames(2, renames);
    assertRenameTo(renames, attributes1, "MyAlbum [2018-08-15] - 001.jpg");
    assertRenameTo(renames, attributes2, "MyAlbum [2018-08-15] - 002.jpg");
  }

  @Test
  public void twoRenamesWithOnlyOneIndex() {
    final Attributes attributes1 = attributes(DAY, TIME, "001");
    final Attributes attributes2 = attributes(DAY, TIME, null);

    final Map<Attributes, String> renames = renames(attributes1, attributes2);
    assertNumberOfRenames(2, renames);
    // Order of renames is not clear, hence only target names can be checked.
    assertRenames(renames, "MyAlbum [2018-08-15] - 001.jpg", "MyAlbum [2018-08-15] - 002.jpg");
  }

  @Test
  public void twoRenamesWithExistingButEqualIndexes() {
    final Attributes attributes1 = attributes(DAY, TIME, "003");
    final Attributes attributes2 = attributes(DAY, TIME, "003");

    final Map<Attributes, String> renames = renames(attributes1, attributes2);
    assertNumberOfRenames(2, renames);
    // Order of renames is not clear, hence only target names can be checked.
    assertRenames(renames, "MyAlbum [2018-08-15] - 001.jpg", "MyAlbum [2018-08-15] - 002.jpg");
  }

  @Test
  public void sortInByTime() {
    final Attributes attributes1 = attributes(DAY, TIME, "001");
    final Attributes attributes2 = attributes(DAY, TIME, null);
    final Attributes attributes3 = attributes(DAY, TIME.plusMinutes(1), "002");

    final Map<Attributes, String> renames = renames(attributes1, attributes2, attributes3);
    assertNumberOfRenames(3, renames);
    assertRenameTo(renames, attributes1, "MyAlbum [2018-08-15] - 001.jpg");
    assertRenameTo(renames, attributes2, "MyAlbum [2018-08-15] - 002.jpg");
    assertRenameTo(renames, attributes3, "MyAlbum [2018-08-15] - 003.jpg");
  }

  @Test
  public void alwaysIndexOneForMultipleDays() {
    final int n = 100;
    final Attributes[] attributes = new Attributes[n];
    for (int i = 0; i < n; i++) {
      attributes[i] = attributes(DAY.plusDays(i), TIME, null);
    }
    final Map<Attributes, String> renames = renames(attributes);
    assertNumberOfRenames(100, renames);
    for (int i = 0; i < n; i++) {
      assertTrue("Wrong index.", renames.get(attributes[i]).endsWith("001.jpg"));
    }
  }

  private Attributes attributes(final LocalDate day, final LocalTime time, final String index) {
    return new Attributes(Paths.get(".", counter++ + ".jpg"), day, time, index);
  }

  private Map<Attributes, String> renames(final Attributes... attributes) {
    final List<Attributes> allAttributes = Arrays.asList(attributes);
    final Map<File, Attributes> lookup = allAttributes.stream()
        .collect(Collectors.toMap(a -> a.getPath().toFile(), a -> a));

    final Map<Attributes, String> renames = new HashMap<>();
    new Renamer(ALBUM, (s, t) -> renames.put(lookup.get(s), t.getName()))
        .accept(allAttributes.stream());
    return renames;
  }

  private void assertNumberOfRenames(final int expectedNumber,
      final Map<Attributes, String> renames) {
    assertEquals("Wrong number of renames.", expectedNumber, renames.size());
  }

  private void assertRenameTo(final Map<Attributes, String> renames, final Attributes attributes,
      final String expectedName) {
    assertTrue("No rename found at all.", renames.containsKey(attributes));
    assertEquals("Wrong rename.", expectedName, renames.get(attributes));
  }

  private void assertRenames(final Map<Attributes, String> names, final String... expectedNames) {
    final Set<String> actual = new HashSet<>(names.values());
    final Set<String> expected = new HashSet<>(Arrays.asList(expectedNames));
    assertEquals("Wrong names.", expected, actual);
  }

}