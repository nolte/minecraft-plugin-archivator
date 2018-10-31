package de.noltarium.minecraft.backup;

import java.nio.file.Path;

import de.noltarium.minecraft.backup.model.BackupProcessStrategyType;
import de.noltarium.minecraft.utils.ArchiveType;

public interface BackupConfigProvider {

	Path getBackupWorkingPath();

	Path getBackupArchivePath();

	BackupProcessStrategyType getBackupStrategy();

	Boolean isReportEnabled();

	String getArchiveNameFormat();

	ArchiveType getArchiveType();
	
	Integer getMaxKeepedBackups();

}
