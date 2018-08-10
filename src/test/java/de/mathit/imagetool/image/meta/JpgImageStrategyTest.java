package de.mathit.imagetool.image.meta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.mathit.imagetool.image.ImageStrategy;
import de.mathit.imagetool.image.meta.JpgImageStrategy;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class JpgImageStrategyTest {

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
    final JpgImageStrategy strategy = new JpgImageStrategy(
        new File("target/test-classes/exif/" + path));
    Assert.assertEquals("Wrong day.", expectedDay, ImageStrategy.day(strategy));
    assertEquals("Wrong time.", expectedTime, ImageStrategy.time(strategy));
    assertNull("Expected no index.", ImageStrategy.index(strategy));
  }

  private void assertNoResult(final String path) {
    final JpgImageStrategy strategy = new JpgImageStrategy(new File(path));
    assertNull("Expected no day.", ImageStrategy.day(strategy));
    assertNull("Expected no time.", ImageStrategy.time(strategy));
    assertNull("Expected no index.", ImageStrategy.index(strategy));
  }

}