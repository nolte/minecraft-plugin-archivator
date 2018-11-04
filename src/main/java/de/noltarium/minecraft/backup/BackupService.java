package de.noltarium.minecraft.backup;

import java.io.File;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import de.noltarium.minecraft.backup.model.BackupAlwaysRunningException;
import de.noltarium.minecraft.chat.ChatFacade;
import de.noltarium.minecraft.database.DatabaseFacade;
import de.noltarium.minecraft.database.model.BackupEntity;

public class BackupService {

	private final BackupConfigProvider config;
	private static Thread thread = null;
	private final ChatFacade chat;
	private final DatabaseFacade databaseFacade;

	public BackupService(BackupConfigProvider config, ChatFacade chat, DatabaseFacade databaseFacade) {
		this.config = config;
		this.chat = chat;
		this.databaseFacade = databaseFacade;
	}

	public void shutdown() {
	}

	public void startFullBackup() throws BackupAlwaysRunningException {
		List<File> worlds = Bukkit.getWorlds().stream().map(w -> w.getWorldFolder()).collect(Collectors.toList());
		startBackup(worlds);

	}

	public List<String> findExistingBackups() {
		return Arrays.asList(config.getBackupArchivePath().toFile().list()).parallelStream()
				.filter(e -> !e.endsWith(".md")).collect(Collectors.toList());

	}

	private void startBackup(List<File> backupSources) throws BackupAlwaysRunningException {
		// check that only one backup Thread exists per time
		if (thread != null && thread.isAlive()) {
			throw new BackupAlwaysRunningException();
		} else {
			OffsetDateTime startTime = OffsetDateTime.now(ZoneOffset.UTC);
			String archiveId = MessageFormat.format(config.getArchiveNameFormat(), Date.from(startTime.toInstant()));
			BackupEntity run = BackupEntity.builder().planedBackupFiles(backupSources).backupRunId(archiveId)
					.startTime(startTime).build();
			databaseFacade.insertNewBackupRun(run);

			thread = new Thread(new BackupRunnable(run, chat, config, databaseFacade));
			thread.start();
		}

	}

}
