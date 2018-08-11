package de.mathit.imagetool.attribute;

import de.mathit.imagetool.attribute.file.GalaxyFileNameAttributeStrategy;
import de.mathit.imagetool.attribute.file.TargetFileNameAttributeStrategy;
import de.mathit.imagetool.attribute.meta.JpgAttributeStrategy;
import de.mathit.imagetool.attribute.meta.MovAttributeStrategy;
import de.mathit.imagetool.attribute.meta.Mp4AttributeStrategy;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Function;

/**
 * Get the attributes of an image given by path.
 */
public class AttributesFunction implements Function<Path, Attributes> {

  @Override
  public Attributes apply(final Path path) {
    final AttributeStrategy[] attributeStrategies = new AttributeStrategy[]{
        new TargetFileNameAttributeStrategy(path), new GalaxyFileNameAttributeStrategy(path),
        new JpgAttributeStrategy(path), new MovAttributeStrategy(path),
        new Mp4AttributeStrategy(path)};
    final LocalDate day = AttributeStrategy.day(attributeStrategies);
    final LocalTime time = AttributeStrategy.time(attributeStrategies);
    final String index = AttributeStrategy.index(attributeStrategies);
    return new Attributes(path, day, time, index);
  }

}