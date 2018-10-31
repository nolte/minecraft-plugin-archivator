package de.noltarium.minecraft.config;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.BACKUP_PROPERTY_PREFIX;

import java.io.File;
import java.nio.file.Path;

import org.bukkit.configuration.file.FileConfiguration;

import de.noltarium.minecraft.backup.BackupConfigProvider;
import de.noltarium.minecraft.backup.model.BackupProcessStrategyType;
import de.noltarium.minecraft.utils.ArchiveType;

public class BackupConfigFileProvider implements BackupConfigProvider {

	public static final String BACKUP_WORKING_DIR_KEY = BACKUP_PROPERTY_PREFIX + "workingtmp";
	public static final String BACKUP_STRATEGY_KEY = BACKUP_PROPERTY_PREFIX + "strategy";
	public static final String BACKUP_MAX_BEFORE_ERASE_KEY = BACKUP_PROPERTY_PREFIX + "maxBackupsBeforeErase";

	private static final String BACKUP_PROPERTY_REPORT_PREFIX = BACKUP_PROPERTY_PREFIX + "report.";
	public static final String BACKUP_REPORT_ENABLED_KEY = BACKUP_PROPERTY_REPORT_PREFIX + "enabled";

	private static final String BACKUP_PROPERTY_ARCHIVE_PREFIX = BACKUP_PROPERTY_PREFIX + "archive.";
	public static final String BACKUP_ARCHIVE_DIR_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + "dir";
	public static final String BACKUP_ARCHIVE_NAME_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + ".name";
	public static final String BACKUP_ARCHIVE_FORMAT_KEY = BACKUP_PROPERTY_ARCHIVE_PREFIX + ".format";

	private static final String BACKUP_PROPERTY_SOURCES_PREFIX = BACKUP_PROPERTY_PREFIX + "source.";
	public static final String BACKUP_SOURCE_WORLD_KEY = BACKUP_PROPERTY_SOURCES_PREFIX + ".workds";

	private final FileConfiguration pluginConfig;
	private final File dataDir;

	public BackupConfigFileProvider(File dataDir,FileConfiguration pluginConfig) {
		this.dataDir = dataDir;
		this.pluginConfig = pluginConfig;
	}

	@Override
	public Boolean isReportEnabled() {
		return pluginConfig.getBoolean(BACKUP_REPORT_ENABLED_KEY);
	}

	@Override
	public String getArchiveNameFormat() {
		return pluginConfig.getString(BACKUP_ARCHIVE_NAME_KEY);
	}
	
	@Override
	public ArchiveType getArchiveType() {
		return ArchiveType.fromString(pluginConfig.getString(BACKUP_ARCHIVE_FORMAT_KEY));
	}


	@Override
	public BackupProcessStrategyType getBackupStrategy() {
		return BackupProcessStrategyType.fromString(pluginConfig.getString(BACKUP_STRATEGY_KEY));
	}

	@Override
	public Path getBackupWorkingPath() {
		return java.nio.file.Paths.get(dataDir.getAbsolutePath(),pluginConfig.getString(BACKUP_WORKING_DIR_KEY));
	}

	@Override
	public Path getBackupArchivePath() {
		return java.nio.file.Paths.get(dataDir.getAbsolutePath(),pluginConfig.getString(BACKUP_ARCHIVE_DIR_KEY));
	}

	@Override
	public Integer getMaxKeepedBackups() {
		return pluginConfig.getInt(BACKUP_MAX_BEFORE_ERASE_KEY);
	}
}
