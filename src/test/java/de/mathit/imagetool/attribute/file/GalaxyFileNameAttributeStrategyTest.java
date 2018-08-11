package de.mathit.imagetool.attribute.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.mathit.imagetool.attribute.AttributeStrategy;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class GalaxyFileNameAttributeStrategyTest {

  @Test
  public void wrongDelimiter() {
    assertNoResult("20180406-102615.jpg");
  }

  @Test
  public void wrongDateFormat() {
    assertNoResult("201804a6_102615.jpg");
  }

  @Test
  public void wrongTimeFormat() {
    assertNoResult("20180406_12615.jpg");
  }

  @Test
  public void correctFilename() {
    assertResult("20180406_102615.jpg", LocalDate.of(2018, 04, 06), LocalTime.of(10, 26, 15));
  }

  @Test
  public void correctFilenameWithPath() {
    assertResult("C:\\target/20180406_172615.JPG", LocalDate.of(2018, 04, 06),
        LocalTime.of(17, 26, 15));
  }

  private void assertResult(final String path, final LocalDate expectedDay,
      final LocalTime expectedTime) {
    final GalaxyFileNameAttributeStrategy strategy = new GalaxyFileNameAttributeStrategy(new File(path));
    Assert.assertEquals("Wrong day.", expectedDay, AttributeStrategy.day(strategy));
    assertEquals("Wrong time.", expectedTime, AttributeStrategy.time(strategy));
    assertNull("Expected no index.", AttributeStrategy.index(strategy));
  }

  private void assertNoResult(final String path) {
    final GalaxyFileNameAttributeStrategy strategy = new GalaxyFileNameAttributeStrategy(new File(path));
    assertNull("Expected no day.", AttributeStrategy.day(strategy));
    assertNull("Expected no time.", AttributeStrategy.time(strategy));
    assertNull("Expected no index.", AttributeStrategy.index(strategy));
  }

}