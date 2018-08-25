package de.mathit.imagetool;

import static org.junit.Assert.assertEquals;

import java.io.File;
import org.junit.Test;

/**
 * Test {@link Album#getName()}.
 */
public class AlbumNameTest {

  private final static String BASE = "target/test-classes/";

  @Test(expected = IllegalArgumentException.class)
  public void notExistingDirectory() {
    new Album("c:/not_existing_path");
  }

  @Test(expected = IllegalArgumentException.class)
  public void file() {
    new Album(BASE + "images/album1/image1.jpg");
  }

  @Test
  public void nameWithoutDate() {
    assertName("album1", BASE + "images/album1");
  }

  @Test
  public void nameWithDate() {
    assertName("album2", BASE + "images/album2 [2015-04-25]");
  }

  @Test
  public void directoryWithSubdirectories() {
    assertName("images", BASE + "images");
  }

  @Test
  public void trailingSlash() {
    assertName("images", BASE + "images/");
  }

  @Test
  public void backslash() {
    assertName("images", BASE.replaceAll("/", "\\\\") + "images\\");
  }

  @Test
  public void absolutePath() {
    assertName("images", new File(BASE + "images").getAbsolutePath());
  }

  private void assertName(final String expectedName, final String path) {
    assertEquals("Wrong name.", expectedName, new Album(path).getName());
  }

}