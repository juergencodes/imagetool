package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;
import de.mathit.imagetool.attribute.Attributes;
import de.mathit.imagetool.attribute.AttributesFunction;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ImageTool {

  public static void main(final String[] args) throws ImageProcessingException, IOException {

    final String path = "D:\\Bilder\\_neu 2018\\02";
    final Stream<Attributes> attributes = Files.list(Paths.get(path))
        .map(new AttributesFunction());

    new Renamer("Mein Album", (p, s) -> System.out.println(p + " -> " + s))
        .accept(attributes);
  }

}