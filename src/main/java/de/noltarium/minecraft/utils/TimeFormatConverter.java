package de.noltarium.minecraft.utils;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.humanReadableDateFormat;

import java.time.OffsetDateTime;

public class TimeFormatConverter {

	public static String convertToReadableString(OffsetDateTime time) {
		return time.format(humanReadableDateFormat);
	}
}
