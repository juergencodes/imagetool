package de.mathit.imagetool;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

/**
 * Function that creates renames for images.
 */
public class Renamer implements Consumer<Stream<Attributes>> {

	private final String prefix;
	private final BiConsumer<File, File> renameCommand;

	public Renamer(final String prefix, final BiConsumer<File, File> renameCommand) {
		this.prefix = prefix;
		this.renameCommand = renameCommand;
	}

	@Override
	public void accept(final Stream<Attributes> attributes) {
		attributes.filter(a -> a.getDay() != null)
				.filter(a -> a.getTime() != null || a.getIndex() != null)
				.collect(groupingBy(Attributes::getDay, toCollection(
						() -> new TreeSet<>(
								(a, b) -> {
									if (a.getIndex() != null && b.getIndex() != null
											&& a.getIndex().compareTo(b.getIndex()) != 0) {
										return a.getIndex().compareTo(b.getIndex());
									} else if (a.getTime() != null && b.getTime() != null
											&& a.getTime().compareTo(b.getTime()) != 0) {
										return a.getTime().compareTo(b.getTime());
									} else {
										return a.getFile().compareTo(b.getFile());
									}
								})))).values()
				.stream().forEachOrdered(attr -> {
			final List<Attributes> reversed = new LinkedList<>(attr);
			Collections.reverse(reversed);
			int index = reversed.size();
			for (final Attributes a : reversed) {
				final String s = a.getFile().toString();
				final String name = String.format("%s [%s] - %03d%s", prefix, a.getDay(), index--,
						s.substring(s.lastIndexOf('.')).toLowerCase());
				final File from = a.getFile();
				renameCommand.accept(from, new File(from.getParentFile(), name));
			}
		});
	}

}