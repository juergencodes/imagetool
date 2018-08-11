package de.mathit.imagetool.attribute.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.mathit.imagetool.attribute.AttributeStrategy;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class JpgAttributeStrategyTest {

  @Test
  public void wrongExtension() {
    assertNoResult("whatever.mov");
  }

  @Test
  public void test() {
    assertResult("image1.jpg", LocalDate.of(2016, 5, 29), LocalTime.of(17, 3, 48));
  }

  private void assertResult(final String path, final LocalDate expectedDay,
      final LocalTime expectedTime) {
    final JpgAttributeStrategy strategy = new JpgAttributeStrategy(Paths.get(
        "target/test-classes/exif/" + path));
    Assert.assertEquals("Wrong day.", expectedDay, AttributeStrategy.day(strategy));
    assertEquals("Wrong time.", expectedTime, AttributeStrategy.time(strategy));
    assertNull("Expected no index.", AttributeStrategy.index(strategy));
  }

  private void assertNoResult(final String path) {
    final JpgAttributeStrategy strategy = new JpgAttributeStrategy(Paths.get(path));
    assertNull("Expected no day.", AttributeStrategy.day(strategy));
    assertNull("Expected no time.", AttributeStrategy.time(strategy));
    assertNull("Expected no index.", AttributeStrategy.index(strategy));
  }

}