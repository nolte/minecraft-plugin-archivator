package de.noltarium.minecraft.utils;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.defaultTimeZone;
import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.time.OffsetDateTime;

import org.junit.Test;

public class TimeFormatConverterTest {

	@Test
	public void convertOffsetToStringDateTest() throws Exception {
		String readableDate = TimeFormatConverter
				.convertToReadableString(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541191505), defaultTimeZone));
		assertEquals("2018-11-02 20:45:05 +0000", readableDate);
	}
}
