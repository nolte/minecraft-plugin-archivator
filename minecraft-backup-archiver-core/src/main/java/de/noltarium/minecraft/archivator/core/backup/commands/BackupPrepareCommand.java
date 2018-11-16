package de.noltarium.minecraft.archivator.core.backup.commands;

import java.io.File;
import java.util.stream.Stream;

import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupPrepareCommand {

	private final BackupFilesCollectorService collectorService;

	public Stream<File> execute(String backupId) throws MissingBackupFilesException {
		return collectorService.collectPossibleBackupFiles();
	}

}
