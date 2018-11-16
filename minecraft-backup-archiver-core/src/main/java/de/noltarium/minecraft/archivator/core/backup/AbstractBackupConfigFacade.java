package de.noltarium.minecraft.archivator.core.backup;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import de.noltarium.minecraft.archivator.core.database.DataSourceFactory;
import lombok.Getter;

public abstract class AbstractBackupConfigFacade implements DatabaseConfig {

	public static final DateTimeFormatter humanReadableDateFormat = DateTimeFormatter.ofPattern("YYY-MM-dd HH:mm:ss Z");
	public static final String NEWLINE = System.lineSeparator();
	public static final ZoneId defaultTimeZone = ZoneOffset.UTC;

	protected static final String BACKUP_PROPERTY_PREFIX = "archivator.";

	protected static final String BACKUP_PROPERTY_WORKDIR_KEY = BACKUP_PROPERTY_PREFIX + "workingtmp";
	protected static final String BACKUP_PROPERTY_MAX_BACKUPS_KEY = BACKUP_PROPERTY_PREFIX + "maxBackupsBeforeErase";

	protected static final String BACKUP_PROPERTY_ARCHIVE_PREFIX = BACKUP_PROPERTY_PREFIX + "archive.";
	protected static final String BACKUP_PROPERTY_ARCHIVE_DIR_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + "dir";
	protected static final String BACKUP_PROPERTY_ARCHIVE_FORMAT_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + "format";
	protected static final String BACKUP_PROPERTY_ARCHIVE_NAME_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + "name";

	protected static final String BACKUP_PROPERTY_DATABASE_PREFIX = BACKUP_PROPERTY_PREFIX + "database.";
	protected static final String BACKUP_PROPERTY_DATABASE_TYPE_KEY = BACKUP_PROPERTY_DATABASE_PREFIX + "type";
	protected static final String BACKUP_PROPERTY_DATABASE_TABLE_PREFIX_KEY = BACKUP_PROPERTY_DATABASE_PREFIX
			+ "table_prefix";
	protected static final String BACKUP_PROPERTY_DATABASE_SQLLITE_FILE_KEY = BACKUP_PROPERTY_DATABASE_PREFIX + "file";

	@Getter
	private final DataSource dataSource;

	@Getter
	private final String tablePrefix;

	public AbstractBackupConfigFacade(DataSourceFactory factory, String tablePrefix) {
		dataSource = factory.createDataSource();
		this.tablePrefix = tablePrefix;
	}

}
