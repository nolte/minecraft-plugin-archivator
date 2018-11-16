package de.noltarium.minecraft.archivator.core.backup;

import java.io.File;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCleanCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupPrepareCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupSaftyPrepareCommand;
import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import de.noltarium.minecraft.archivator.core.services.CopyFilesToFolderService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;
import de.noltarium.minecraft.archivator.core.services.FolderCleanService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupProcessFactory {

	private final FilesCompressService compressService;
	private final CopyFilesToFolderService copyService;
	private final BackupConfig config;
	private final BackupDAO backupDAO;

	private final List<ProcessListener<String>> backupStartedListener;
	private final List<ProcessListener<File>> backupFinishedListener;
	private final List<ProcessListener<Exception>> backupFaildListener;

	public BackupProcess createBackupProcess() {
		File archiveDir;
		archiveDir = config.getArchiveDir();

		Integer maxReleases = config.getMaxKeepedReleases();
		ArchiveType archivingType = config.getArchivingType();
		File copyWoringDir = config.getCopyWoringDir();
		BackupFilesCollectorService collectorService = config.getCollectorService();

		final BackupArchiveCleanCommand cleanup = new BackupArchiveCleanCommand(archiveDir,
				new FolderCleanService(maxReleases));

		archiveDir.mkdirs();
		final BackupArchiveCommand archiving = new BackupArchiveCommand(compressService, archivingType, archiveDir);

		final BackupPrepareCommand prepare = new BackupSaftyPrepareCommand(copyWoringDir, copyService,
				collectorService);

		OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.UTC);
		String backupId = MessageFormat.format(config.getArchiveNameFormat(), Date.from(startTime.toInstant()));

		return new BackupProcess(prepare, archiving, cleanup, backupDAO, backupId, backupStartedListener,
				backupFinishedListener, backupFaildListener);
	}
}
