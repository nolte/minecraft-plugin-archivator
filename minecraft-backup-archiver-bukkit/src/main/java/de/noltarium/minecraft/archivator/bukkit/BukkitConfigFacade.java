package de.noltarium.minecraft.archivator.bukkit;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade;
import de.noltarium.minecraft.archivator.core.backup.BackupConfig;
import de.noltarium.minecraft.archivator.core.database.DataSourceFactory;
import de.noltarium.minecraft.archivator.core.database.SQLiteDataSourceFactory;
import de.noltarium.minecraft.archivator.core.database.SupportedDatabaseType;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;

//@AllArgsConstructor
public class BukkitConfigFacade extends AbstractBackupConfigFacade implements BackupConfig {

	public BukkitConfigFacade(FileConfiguration config, File pluginDir) throws InvalidConfigurationException {
		super(createDatasourceFactory(config, pluginDir), config.getString(BACKUP_PROPERTY_DATABASE_TABLE_PREFIX_KEY));
		this.config = config;
		this.pluginDir = pluginDir;
	}

	private static DataSourceFactory createDatasourceFactory(FileConfiguration config, File pluginDir)
			throws InvalidConfigurationException {
		SupportedDatabaseType type = SupportedDatabaseType
				.fromString(config.getString(BACKUP_PROPERTY_DATABASE_TYPE_KEY));
		switch (type) {
		case SQLITE:
			String sqlLiteFile = config.getString(BACKUP_PROPERTY_DATABASE_SQLLITE_FILE_KEY);
			if (!Paths.get(sqlLiteFile).isAbsolute())
				sqlLiteFile = Paths.get(pluginDir.getPath(), sqlLiteFile).toFile().getPath();

			SQLiteDataSourceFactory factory = new SQLiteDataSourceFactory(sqlLiteFile);
			return factory;
		default:
			throw new InvalidConfigurationException();
		}
	}

	private final FileConfiguration config;

	private final File pluginDir;

	@Override
	public File getArchiveDir() {
		File file = new File(config.getString(BACKUP_PROPERTY_ARCHIVE_DIR_KEY));
		if (!file.isAbsolute())
			file = Paths.get(pluginDir.getPath(), file.getPath()).toFile();

		return file;
	}

	@Override
	public Integer getMaxKeepedReleases() {
		return config.getInt(BACKUP_PROPERTY_MAX_BACKUPS_KEY);
	}

	@Override
	public ArchiveType getArchivingType() {
		return ArchiveType.fromString(config.getString(BACKUP_PROPERTY_ARCHIVE_FORMAT_KEY));
	}

	@Override
	public File getCopyWoringDir() {

		File file = new File(config.getString(BACKUP_PROPERTY_WORKDIR_KEY));
		if (!file.isAbsolute())
			file = Paths.get(pluginDir.getPath(), file.getPath()).toFile();

		return file;
	}

	@Override
	public BackupFilesCollectorService getCollectorService() {
		return new BackupFilesCollectorService(Arrays.asList(new BukkitWorldsCollector()));
	}

	@Override
	public String getArchiveNameFormat() {
		return config.getString(BACKUP_PROPERTY_ARCHIVE_NAME_KEY);
	}

}
