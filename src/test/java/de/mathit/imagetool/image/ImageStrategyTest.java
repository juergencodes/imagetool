package de.mathit.imagetool.image;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;
import org.junit.Test;

public class ImageStrategyTest {

  private final static LocalDate DAY = LocalDate.now();
  private final static LocalTime TIME = LocalTime.now();
  private final static String INDEX = "myIndex";

  private final ImageStrategy strategyDay = strategy(i -> i.registerDay(DAY));
  private final ImageStrategy strategyTime = strategy(i -> i.registerTime(TIME));
  private final ImageStrategy strategyIndex = strategy(i -> i.registerIndex(INDEX));

  @Test
  public void testOnlyDay() {
    assertResult(DAY, null, null, strategyDay);
  }

  @Test
  public void testOnlyTime() {
    assertResult(null, TIME, null, strategyTime);
  }

  @Test
  public void testOnlyIndex() {
    assertResult(null, null, INDEX, strategyIndex);
  }

  @Test
  public void testAll() {
    assertResult(DAY, TIME, INDEX, strategyTime, strategyDay, strategyIndex);
  }

  private void assertResult(final LocalDate day, final LocalTime time, final String index,
      final ImageStrategy... strategies) {
    assertEquals("Wrong day.", day, ImageStrategy.day(strategies));
    assertEquals("Wrong time.", time, ImageStrategy.time(strategies));
    assertEquals("Wrong index.", index, ImageStrategy.index(strategies));
  }

  private ImageStrategy strategy(final Consumer<ImageStrategy> consumer) {
    return new ImageStrategy(null) {
      @Override
      protected void init(final File path) {
        consumer.accept(this);
      }
    };
  }


}