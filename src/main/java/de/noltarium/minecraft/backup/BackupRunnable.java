package de.noltarium.minecraft.backup;

import java.util.Optional;

import de.noltarium.minecraft.backup.model.BackupProcessStrategyType;
import de.noltarium.minecraft.backup.steps.ArchiveBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchiveFolderCleanService;
import de.noltarium.minecraft.backup.steps.ArchiveTempBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;
import de.noltarium.minecraft.backup.strategy.AbstractBackupStrategy;
import de.noltarium.minecraft.backup.strategy.DirectBackupStrategy;
import de.noltarium.minecraft.backup.strategy.SaftyBackupStrategy;
import de.noltarium.minecraft.chat.ChatNotification;
import de.noltarium.minecraft.database.DatabaseFacade;
import de.noltarium.minecraft.database.model.BackupEntity;

public class BackupRunnable implements Runnable {

	private final ChatNotification chat;
	private final BackupConfigProvider config;
	private final BackupEntity backupEntity;
	private final DatabaseFacade databaseFacade;

	public BackupRunnable(BackupEntity backupEntity, ChatNotification chat, BackupConfigProvider config,
			DatabaseFacade databaseFacade) {
		super();
		this.backupEntity = backupEntity;
		this.chat = chat;
		this.config = config;
		this.databaseFacade = databaseFacade;
	}

	@Override
	public void run() {
		chat.msgAdmins("Starting Backup");

		BackupProcessStrategyType type = config.getBackupStrategy();

		AbstractBackupStrategy<?> backupStrategy = null;

		Optional<ArchiveFolderCleanService> cleanService = Optional.empty();
		// only clean the archive if a max keeped size configured
		if (config.getMaxKeepedBackups() != null && config.getMaxKeepedBackups() > 0) {
			cleanService = Optional.of(new ArchiveFolderCleanService(config.getMaxKeepedBackups()));
		}

		switch (type) {
		case DIRECT:
			ArchiveBaseFolderPreparation folderPrep = new ArchiveBaseFolderPreparation(config.getBackupArchivePath(),
					cleanService);
			ArchivingStep archiving = new ArchivingStep(folderPrep, config.getArchiveType());

			backupStrategy = new DirectBackupStrategy(backupEntity, archiving, folderPrep);
			break;
		case SAFTY:
			ArchiveTempBaseFolderPreparation folderTmpPrep = new ArchiveTempBaseFolderPreparation(
					config.getBackupArchivePath(), config.getBackupWorkingPath(), cleanService);
			ArchivingStep archivingWithTmp = new ArchivingStep(folderTmpPrep, config.getArchiveType());
			backupStrategy = new SaftyBackupStrategy(backupEntity, archivingWithTmp, folderTmpPrep);
			break;
		default:
			chat.msgAdmins("Backup \"" + backupEntity.getBackupRunId() + "\" faild ...");
			throw new IllegalArgumentException("not supported Strategy Type" + type);
		}

		backupStrategy.executeProcess();
		databaseFacade.updateBackupRun(backupEntity);
		databaseFacade.updateLastSuccessFullRun(backupEntity.getStartTime());
		chat.msgAdmins("Backup \"" + backupEntity.getBackupRunId() + "\" finished ...");
	}

}
