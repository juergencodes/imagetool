package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;
import java.io.IOException;

public class ImageTool {

  public static void main(final String[] args) throws ImageProcessingException, IOException {

    final String path = "D:\\Bilder\\_neu 2018\\02";
    final Album album = new Album(path);

    new Renamer(album.getName(), (p, s) -> System.out.println(p + " -> " + s))
        .accept(album.getFiles().map(new AttributesFunction()));
  }

}