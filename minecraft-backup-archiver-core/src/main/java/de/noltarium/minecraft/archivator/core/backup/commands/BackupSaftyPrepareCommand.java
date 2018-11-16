package de.noltarium.minecraft.archivator.core.backup.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import de.noltarium.minecraft.archivator.core.model.BackupCopyModel;
import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import de.noltarium.minecraft.archivator.core.services.CopyFilesToFolderService;
import lombok.extern.java.Log;

@Log
public class BackupSaftyPrepareCommand extends BackupPrepareCommand {

	private final File copyWoringDir;
	private final CopyFilesToFolderService copyService;

	public BackupSaftyPrepareCommand(File copyWoringDir, CopyFilesToFolderService copyService,
			BackupFilesCollectorService collectorService) {
		super(collectorService);
		this.copyWoringDir = copyWoringDir;
		this.copyService = copyService;

	}

	@Override
	public Stream<File> execute(String backupId) throws MissingBackupFilesException {
		File backupWorkingRunDir = createBackupTempWorking(backupId);
		backupWorkingRunDir.mkdirs();
		List<BackupCopyModel> copiedFiles = copyService.copyFilesToFolder(super.execute(backupId), backupWorkingRunDir)
				.collect(Collectors.toList());
		// check some erros happen
		// check faild from copy job
		if (copiedFiles.parallelStream().filter(file -> file.getFaildReason().isPresent()).count() > 0) {
			log.severe("some files copiedFiles.parallelStream()faild to copy ");
			throw new FaildToCopyFilesException();
		} else {
			// get selected files
			Stream<File> filesForArchive = copiedFiles.parallelStream().map(file -> file.getTarget());
			return filesForArchive;
		}
	}

	private File createBackupTempWorking(String backupId) {
		File backupWorkingRunDir = Paths.get(copyWoringDir.getPath(), backupId).toFile();
		return backupWorkingRunDir;
	}

	public void removeTempFolder(String backupId) {
		File backupWorkingRunDir = createBackupTempWorking(backupId);
		if (backupWorkingRunDir.exists())
			try {
				FileUtils.deleteDirectory(backupWorkingRunDir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}

	public class FaildToCopyFilesException extends MissingBackupFilesException {

		private static final long serialVersionUID = 1L;

	}
}
