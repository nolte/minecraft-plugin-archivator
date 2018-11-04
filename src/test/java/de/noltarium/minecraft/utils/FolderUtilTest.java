package de.noltarium.minecraft.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FolderUtilTest {

	@Test
	public void convertMinimalSize() throws Exception {
		assertEquals("0 B", FolderUtil.humanReadableByteCount(0, true));
		assertEquals("0 B", FolderUtil.humanReadableByteCount(0, false));
	}

	@Test
	public void convertSimpleOneMegabyte() throws Exception {
		assertEquals("1.0 MB", FolderUtil.humanReadableByteCount(1024 * 1024, true));
		assertEquals("1.0 MiB", FolderUtil.humanReadableByteCount(1024 * 1024, false));
	}

	@Test
	public void convertSimpleOneGigabyte() throws Exception {
		assertEquals("1.1 GB", FolderUtil.humanReadableByteCount(1024 * 1024 * 1024, true));
		assertEquals("1.0 GiB", FolderUtil.humanReadableByteCount(1024 * 1024 * 1024, false));
	}

	@Test
	public void convertSimpleLongMaxValue() throws Exception {
		assertEquals("9.2 EB", FolderUtil.humanReadableByteCount(Long.MAX_VALUE, true));
		assertEquals("8.0 EiB", FolderUtil.humanReadableByteCount(Long.MAX_VALUE, false));
	}
}
