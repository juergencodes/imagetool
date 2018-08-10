package de.mathit.imagetool.image.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.mathit.imagetool.image.ImageStrategy;
import de.mathit.imagetool.image.file.TargetFileNameImageStrategy;
import java.io.File;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class TargetFileNameImageStrategyTest {

  @Test
  public void unknownName() {
    assertNoResult("anyname 4711 - 001.jpg");
  }

  @Test
  public void wrongBrackets() {
    assertNoResult("Your Album {2015-04-25} - 001.jpg");
  }

  @Test
  public void wrongDateFormat() {
    assertNoResult("Your Album [2015-4-25] - 001.jpg");
  }

  @Test
  public void wrongDelimiter() {
    assertNoResult("Your Album [2015_04_25] - 001.jpg");
  }

  @Test
  public void correctFilename() {
    assertResult("My Album [2015-04-25] - 001.jpg", LocalDate.of(2015, 04, 25), "001");
  }

  @Test
  public void correctFilenameWithPath() {
    assertResult("C:\\target/My Album [2015-04-25] - 002.JPG", LocalDate.of(2015, 04, 25), "002");
  }

  private void assertResult(final String path, final LocalDate expectedDay,
      final String expectedIndex) {
    final TargetFileNameImageStrategy strategy = new TargetFileNameImageStrategy(new File(path));
    Assert.assertEquals("Wrong day.", expectedDay, ImageStrategy.day(strategy));
    assertEquals("Wrong index.", expectedIndex, ImageStrategy.index(strategy));
    assertNull("Expected no time.", ImageStrategy.time(strategy));
  }

  private void assertNoResult(final String path) {
    final TargetFileNameImageStrategy strategy = new TargetFileNameImageStrategy(new File(path));
    assertNull("Expected no day.", ImageStrategy.day(strategy));
    assertNull("Expected no time.", ImageStrategy.time(strategy));
    assertNull("Expected no index.", ImageStrategy.index(strategy));
  }

}