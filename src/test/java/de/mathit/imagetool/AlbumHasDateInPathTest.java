package de.mathit.imagetool;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link Album#hasDateInPath()}.
 */
public class AlbumHasDateInPathTest {

	private final static String BASE = "target/test-classes/";

	@Test
	public void nameWithoutDate() {
		assertHasNot(BASE + "images/album1");
	}

	@Test
	public void nameWithDate() {
		assertHas(BASE + "images/album2 [2015-04-25]");
	}

	private void assertHasNot(final String path) {
		assertFalse("Expected to have no date in path.", new Album(path).hasDateInPath());
	}

	private void assertHas(final String path) {
		assertTrue("Expected to not have date in path.", new Album(path).hasDateInPath());
	}

}