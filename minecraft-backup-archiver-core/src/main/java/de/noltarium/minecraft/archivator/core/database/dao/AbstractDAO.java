package de.noltarium.minecraft.archivator.core.database.dao;

import static de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade.defaultTimeZone;

import java.sql.Date;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.logging.Logger;

import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractDAO {

	@Getter
	private final DataSource datasource;

	@Getter
	private final String tablePrefix;

	// using the bukkit plugin logger ...
	@Getter
	private final Logger logger;

	protected OffsetDateTime toOffsetDate(Date date) {
		if (date == null)
			return null;
		else
			return OffsetDateTime.ofInstant(Instant.ofEpochSecond(date.getTime()), defaultTimeZone);
	}

	protected Date toSQLDate(OffsetDateTime date) {
		return new Date(date.toEpochSecond());
	}

}
