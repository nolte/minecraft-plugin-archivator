package de.noltarium.minecraft.archivator.core.backup;

import java.io.File;

import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;

public interface BackupConfig {

	public File getArchiveDir();

	public Integer getMaxKeepedReleases();

	public ArchiveType getArchivingType();

	public File getCopyWoringDir();

	public BackupFilesCollectorService getCollectorService();

	public String getArchiveNameFormat();

}
