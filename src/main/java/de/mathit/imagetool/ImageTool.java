package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;
import de.mathit.imagetool.attribute.AttributesFunction;
import de.mathit.imagetool.rename.RenameQueue;
import de.mathit.imagetool.rename.ScriptCreatingRenameQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageTool {

  public static void main(final String[] args) throws ImageProcessingException, IOException {

    final String path = "D:\\Bilder\\_neu 2018\\02";
    Files.list(Paths.get(path)).map(new AttributesFunction()).forEach(a -> {
      System.out.println(a);
    });

  }

  private static RenameQueue fillRenameQueue(final String path) throws IOException {
    final RenameQueue renameQueue = new ScriptCreatingRenameQueue();
    Files.list(Paths.get(path)).forEach(f -> {
      final File file = new File(path, f.getFileName().toString());
      //try {
      renameQueue.add(file, file(path, "new name"));
      //} catch (final IOException e) {
      //    throw new IllegalStateException("Cannot handle file.", e);
      //}
    });
    return renameQueue;
  }

  private static File file(final String path, final String name) {
    return name == null ? null : new File(path, name);
  }

}