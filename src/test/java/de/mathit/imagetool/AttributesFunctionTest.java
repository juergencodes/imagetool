package de.mathit.imagetool;

import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test {@link AttributesFunction}.
 */
public class AttributesFunctionTest {

	@Test
	public void unknownNameAndWrongExtensionForExif() {
		assertNoResult("whatever.mov");
	}

	@Test
	public void exif() {
		assertDayAndTime("target/test-classes/images/album1/image1.jpg", LocalDate.of(2016, 5, 29),
				LocalTime.of(17, 3, 48));
	}

	@Test
	public void noKnownNameNoExif() {
		assertNoResult("image_without_anything.jpg");
	}

	@Test
	public void galaxyWrongDelimiter() {
		assertNoResult("20180406-102615.jpg");
	}

	@Test
	public void galaxyWrongDateFormat() {
		assertNoResult("201804a6_102615.jpg");
	}

	@Test
	public void galaxyWrongTimeFormat() {
		assertNoResult("20180406_12615.jpg");
	}

	@Test
	public void galaxyCorrectFilename() {
		assertDayAndTime("20180406_102615.jpg", LocalDate.of(2018, 04, 06), LocalTime.of(10, 26, 15));
	}

	@Test
	public void galaxyCorrectFilenameWithPath() {
		assertDayAndTime("C:\\target/20180406_172615.JPG", LocalDate.of(2018, 04, 06),
				LocalTime.of(17, 26, 15));
	}

	@Test
	public void targetUnknownName() {
		assertNoResult("anyname 4711 - 001.jpg");
	}

	@Test
	public void targetWrongBrackets() {
		assertNoResult("Your Album {2015-04-25} - 001.jpg");
	}

	@Test
	public void targetWrongDateFormat() {
		assertNoResult("Your Album [2015-4-25] - 001.jpg");
	}

	@Test
	public void targetWrongDelimiter() {
		assertNoResult("Your Album [2015_04_25] - 001.jpg");
	}

	@Test
	public void targetCorrectFilename() {
		assertDayAndIndex("My Album [2015-04-25] - 001.jpg", LocalDate.of(2015, 04, 25), "001");
	}

	@Test
	public void targetCorrectFilenameWithPath() {
		assertDayAndIndex("C:\\target/My Album [2015-04-25] - 002.JPG", LocalDate.of(2015, 04, 25),
				"002");
	}

	@Test
	public void targetOverrulesExif() {
		assertDayAndTimeAndIndex(
				"target/test-classes/images/album2 [2015-04-25]/My Album [2015-04-25] - 007.jpg",
				LocalDate.of(2015, 4, 25),
				LocalTime.of(17, 3, 48), "007");
	}

	@Test
	public void targetNameForMTSFile() {
		assertDayAndIndex("My Video [2018-05-28] - 039a.mts", LocalDate.of(2018, 5, 28), "039a");
	}

	private void assertDayAndTimeAndIndex(final String path, final LocalDate expectedDay,
										  final LocalTime expectedTime,
										  final String expectedIndex) {
		final Attributes attributes = new AttributesFunction().apply(new File(path));
		assertEquals("Wrong day.", expectedDay, attributes.getDay());
		assertEquals("Wrong time.", expectedTime, attributes.getTime());
		assertEquals("Wrong index.", expectedIndex, attributes.getIndex());
	}

	private void assertDayAndIndex(final String path, final LocalDate expectedDay,
								   final String expectedIndex) {
		final Attributes attributes = new AttributesFunction().apply(new File(path));
		assertEquals("Wrong day.", expectedDay, attributes.getDay());
		assertNull("Expected no time.", attributes.getTime());
		assertEquals("Wrong index.", expectedIndex, attributes.getIndex());
	}

	private void assertDayAndTime(final String path, final LocalDate expectedDay,
								  final LocalTime expectedTime) {
		final Attributes attributes = new AttributesFunction().apply(new File(path));
		assertEquals("Wrong day.", expectedDay, attributes.getDay());
		assertEquals("Wrong time.", expectedTime, attributes.getTime());
		assertNull("Expected no index.", attributes.getIndex());
	}

	private void assertNoResult(final String path) {
		final Attributes attributes = new AttributesFunction().apply(new File(path));
		assertNull("Expected no day.", attributes.getDay());
		assertNull("Expected no time.", attributes.getTime());
		assertNull("Expected no index.", attributes.getIndex());
	}

}