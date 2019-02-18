package de.mathit.imagetool;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link Album#getFiles()}.
 */
public class AlbumFileTest {

	private final static String BASE = "target/test-classes/";

	@Test
	public void emptyDirectory() {
		assertEmpty(BASE + "images/album3");
	}

	@Test
	public void singleDirectory1() {
		assertFiles(BASE + "images/album1", "image1.jpg");
	}

	@Test
	public void singleDirectory2() {
		assertFiles(BASE + "images/album2 [2015-04-25]", "My Album [2015-04-25] - 007.jpg");
	}

	@Test
	public void subDirectoryies() {
		assertFiles(BASE + "images", "image1.jpg", "My Album [2015-04-25] - 007.jpg");
	}

	private void assertEmpty(final String path) {
		assertTrue("Expected no files.", getFiles(path).isEmpty());
	}

	private void assertFiles(final String path, final String... files) {
		final List<File> allFiles = getFiles(path);
		assertEquals("Wrong amount of files.", files.length, allFiles.size());
		for (final String expectedFile : files) {
			boolean found = false;
			for (final File file : allFiles) {
				found |= file.getPath().endsWith(expectedFile);
			}
			assertTrue("File " + expectedFile + "' not found.", found);
		}
	}

	private List<File> getFiles(final String path) {
		return new Album(path).getFiles().collect(Collectors.toList());
	}

}