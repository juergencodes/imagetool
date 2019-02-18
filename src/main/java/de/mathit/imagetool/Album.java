package de.mathit.imagetool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Album as it is stored on the hard disc.
 */
public class Album {

	private static final Pattern PATTERN = Pattern
			.compile("(.*)([ ][\\[][0-9]{4}[-][0-9]{2}[-][0-9]{2}[\\]])");

	private final File directory;
	private final String name;

	public Album(final String path) {
		directory = new File(path);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Path " + path + " is not a directory.");
		}
		if (!directory.exists()) {
			throw new IllegalArgumentException("Path " + path + " does not exist.");
		}
		final String lastElement = directory.getAbsolutePath()
				.substring(directory.getAbsolutePath().lastIndexOf(File.separator) + 1);
		final Matcher matcher = PATTERN.matcher(lastElement);
		if (matcher.matches()) {
			name = matcher.group(1);
		} else {
			name = lastElement;
		}
	}

	public String getName() {
		return name;
	}

	public Stream<File> getFiles() {
		try {
			return Files.walk(Paths.get(directory.getAbsolutePath())).map(Path::toFile).filter(f -> Arrays
					.asList("jpg", "jpeg", "mov", "mp4", "mts")
					.contains(f.getPath().substring(f.getPath().lastIndexOf('.') + 1).toLowerCase()));
		} catch (final IOException e) {
			return Stream.empty();
		}
	}

}