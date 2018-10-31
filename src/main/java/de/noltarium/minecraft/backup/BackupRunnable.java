package de.noltarium.minecraft.backup;

import java.io.File;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import de.noltarium.minecraft.backup.model.BackupProcessStrategyType;
import de.noltarium.minecraft.backup.steps.ArchiveBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchiveTempBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;
import de.noltarium.minecraft.backup.strategy.AbstractBackupStrategy;
import de.noltarium.minecraft.backup.strategy.DirectBackupStrategy;
import de.noltarium.minecraft.backup.strategy.SaftyBackupStrategy;
import de.noltarium.minecraft.chat.ChatNotification;
import de.noltarium.minecraft.database.DatabaseFacade;

public class BackupRunnable implements Runnable {

	private final ChatNotification chat;
	private final BackupConfigProvider config;
	private final List<File> backupSources;
	private final DatabaseFacade databaseFacade;

	public BackupRunnable(List<File> backupSources, ChatNotification chat, BackupConfigProvider config,
			DatabaseFacade databaseFacade) {
		super();
		this.backupSources = backupSources;
		this.chat = chat;
		this.config = config;
		this.databaseFacade = databaseFacade;
	}

	@Override
	public void run() {
		chat.msgAdmins("Starting Backup");

		OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.UTC);

		String archiveId = MessageFormat.format(config.getArchiveNameFormat(), Date.from(startTime.toInstant()));

		BackupProcessStrategyType type = config.getBackupStrategy();

		AbstractBackupStrategy<?> backupStrategy = null;
		switch (type) {
		case DIRECT:
			ArchiveBaseFolderPreparation folderPrep = new ArchiveBaseFolderPreparation(config.getBackupArchivePath(),
					config.getMaxKeepedBackups());
			ArchivingStep archiving = new ArchivingStep(folderPrep, config.getArchiveType());

			backupStrategy = new DirectBackupStrategy(archiveId, backupSources, archiving, folderPrep);
			break;
		case SAFTY:
			ArchiveTempBaseFolderPreparation folderTmpPrep = new ArchiveTempBaseFolderPreparation(
					config.getBackupArchivePath(), config.getBackupWorkingPath(), config.getMaxKeepedBackups());
			ArchivingStep archivingWithTmp = new ArchivingStep(folderTmpPrep, config.getArchiveType());
			backupStrategy = new SaftyBackupStrategy(archiveId, backupSources, archivingWithTmp, folderTmpPrep);
			break;
		default:
			chat.msgAdmins("Backup \"" + archiveId + "\" faild ...");
			throw new IllegalArgumentException("not supported Strategy Type" + type);
		}

		backupStrategy.executeProcess();
		databaseFacade.updateLastSuccessFullRun(startTime);
		chat.msgAdmins("Backup \"" + archiveId + "\" finished ...");
	}

}
