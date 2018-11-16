package de.noltarium.minecraft.archivator.core.backup;

import static de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade.defaultTimeZone;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCleanCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupPrepareCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupSaftyPrepareCommand;
import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;
import de.noltarium.minecraft.archivator.core.database.model.BackupEntity;
import de.noltarium.minecraft.archivator.core.database.model.BackupEntity.BackupEntityBuilder;
import de.noltarium.minecraft.archivator.core.model.ArchivingCleanFaildException;
import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class BackupProcess implements Runnable {

	private final BackupPrepareCommand prepare;
	private final BackupArchiveCommand archiving;
	private final BackupArchiveCleanCommand cleanup;
	private final BackupDAO backupDAO;

	@Getter
	private final String backupId;

	private final List<ProcessListener<String>> backupStartedListener;
	private final List<ProcessListener<File>> backupFinishedListener;
	private final List<ProcessListener<Exception>> backupFaildListener;

	@Override
	public void run() {
		try {
			OffsetDateTime now = OffsetDateTime.now(defaultTimeZone);
			BackupEntityBuilder builder = BackupEntity.builder().backupRunId(backupId).startTime(now);

			BackupEntity build = builder.build();
			backupDAO.insertNewBackupRun(build);
			notifyStarting(backupId);
			Stream<File> archiveFiles = prepare.execute(backupId);
			File archive = archiving.execute(archiveFiles, backupId);
			Stream<File> removedArchives = cleanup.execute();
			removedArchives.forEach(removed -> backupDAO
					.updateBackupRunAsRemoved(FilenameUtils.getBaseName(removed.getName()), now, backupId));

			build.setEndTime(OffsetDateTime.now(defaultTimeZone));
			backupDAO.updateBackupRun(build);
			notifyFinished(archive);
		} catch (MissingBackupFilesException | IOException | ArchivingCleanFaildException e) {
			notifyFaild(e);
		} finally {
			if (prepare instanceof BackupSaftyPrepareCommand)
				((BackupSaftyPrepareCommand) prepare).removeTempFolder(backupId);
		}
	}

	private void notifyFaild(Exception e) {
		backupFaildListener.forEach(listener -> listener.receiveProcessState(e));
	}

	private void notifyFinished(File archive) {
		backupFinishedListener.forEach(listener -> listener.receiveProcessState(archive));
	}

	private void notifyStarting(String backupId2) {
		backupStartedListener.forEach(listener -> listener.receiveProcessState(backupId2));

	}

	enum ProcessState {
		PLANED, PROCESSING, FINISHED, FAILD
	}

}
