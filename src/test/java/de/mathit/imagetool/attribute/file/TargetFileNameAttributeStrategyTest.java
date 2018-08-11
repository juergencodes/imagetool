package de.mathit.imagetool.attribute.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.mathit.imagetool.attribute.AttributeStrategy;
import java.io.File;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class TargetFileNameAttributeStrategyTest {

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
    final TargetFileNameAttributeStrategy strategy = new TargetFileNameAttributeStrategy(new File(path));
    Assert.assertEquals("Wrong day.", expectedDay, AttributeStrategy.day(strategy));
    assertEquals("Wrong index.", expectedIndex, AttributeStrategy.index(strategy));
    assertNull("Expected no time.", AttributeStrategy.time(strategy));
  }

  private void assertNoResult(final String path) {
    final TargetFileNameAttributeStrategy strategy = new TargetFileNameAttributeStrategy(new File(path));
    assertNull("Expected no day.", AttributeStrategy.day(strategy));
    assertNull("Expected no time.", AttributeStrategy.time(strategy));
    assertNull("Expected no index.", AttributeStrategy.index(strategy));
  }

}