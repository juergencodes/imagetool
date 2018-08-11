package de.mathit.imagetool.attribute;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;
import org.junit.Test;

public class AttributeStrategyTest {

  private final static LocalDate DAY = LocalDate.now();
  private final static LocalTime TIME = LocalTime.now();
  private final static String INDEX = "myIndex";

  private final AttributeStrategy strategyDay = strategy(i -> i.registerDay(DAY));
  private final AttributeStrategy strategyTime = strategy(i -> i.registerTime(TIME));
  private final AttributeStrategy strategyIndex = strategy(i -> i.registerIndex(INDEX));

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
      final AttributeStrategy... strategies) {
    assertEquals("Wrong day.", day, AttributeStrategy.day(strategies));
    assertEquals("Wrong time.", time, AttributeStrategy.time(strategies));
    assertEquals("Wrong index.", index, AttributeStrategy.index(strategies));
  }

  private AttributeStrategy strategy(final Consumer<AttributeStrategy> consumer) {
    return new AttributeStrategy(null) {
      @Override
      protected void init(final File path) {
        consumer.accept(this);
      }
    };
  }


}