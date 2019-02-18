package de.mathit.imagetool;

import com.drew.imaging.ImageProcessingException;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ImageTool {

	public static void main(final String[] args) throws ImageProcessingException, IOException {
		if (args.length == 0) {
			throw new IllegalArgumentException("Expected a path as first parameter.");
		}
		if (args.length == 1) {
			throw new IllegalArgumentException("Expected a command as second parameter");
		}
		final BiConsumer<File, File> command =
				args[1].equalsIgnoreCase("rename") ? renamer() : printer();

		final String path = args[0];
		final Album album = new Album(path);

		new Renamer(album.getName(), command)
				.accept(album.getFiles().map(new AttributesFunction()));
	}

	private static BiConsumer<File, File> printer() {
		return (p, s) -> System.out.println(p + " -> " + s);
	}

	private static BiConsumer<File, File> renamer() {
		return (p, s) -> p.renameTo(s);
	}

}