package de.noltarium.minecraft.archivator.core.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.noltarium.minecraft.archivator.core.backup.BackupFilesCollector;
import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@AllArgsConstructor
public class BackupFilesCollectorService {

	private final List<BackupFilesCollector> collectors;

	public Stream<File> collectPossibleBackupFiles() throws MissingBackupFilesException {
		List<File> fullList = new ArrayList<>();
		for (BackupFilesCollector backupFilesCollector : collectors) {
			fullList.addAll(backupFilesCollector.loadFiles());
		}

		// throw exception if no Files for backup
		if (fullList.size() <= 0) {
			log.severe("Faild to collect some files for the backup");
			throw new MissingBackupFilesException();
		}

		return fullList.parallelStream();
	}

}
