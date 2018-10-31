package de.noltarium.minecraft;

import static org.junit.Assert.assertNotNull;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.junit.Test;

import de.noltarium.minecraft.utils.TimeFormatConverter;

public class TimeFormatConverterTest {

	@Test
	public void convertOffsetToStringDateTest() throws Exception {
		String readableDate = TimeFormatConverter.convertToReadableString(OffsetDateTime.now(ZoneId.of("UTC")));
		assertNotNull(readableDate);
		System.out.println(readableDate);
	}
}
