package de.mathit.imagetool;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ImageTool {

	public static void main(final String[] args) {
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
		final List<Attributes> attributes = album.getFiles().map(new AttributesFunction()).collect(Collectors.toList());

		new Renamer(album.getName(), command).accept(attributes.stream());

		if (!album.hasDateInPath()) {
			final Set<LocalDate> dates = attributes.stream().map(a -> a.getDay()).filter(d -> d != null).collect(Collectors.toSet());
			System.out.println(dates);
			if (dates.size() == 1) {
				album.getDirectory().renameTo(new File(String.format("%s [%s]", album.getDirectory().getAbsolutePath(), dates.iterator().next())));
			}
		}

	}

	private static BiConsumer<File, File> printer() {
		return (p, s) -> System.out.println(p + " -> " + s);
	}

	private static BiConsumer<File, File> renamer() {
		return (p, s) -> p.renameTo(s);
	}

}