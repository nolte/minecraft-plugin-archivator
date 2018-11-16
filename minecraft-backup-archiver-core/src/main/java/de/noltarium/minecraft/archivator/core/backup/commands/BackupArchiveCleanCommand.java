package de.noltarium.minecraft.archivator.core.backup.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.noltarium.minecraft.archivator.core.model.ArchivingCleanFaildException;
import de.noltarium.minecraft.archivator.core.services.FolderCleanService;
import de.noltarium.minecraft.archivator.core.services.FolderCleanService.FileDeleteState;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupArchiveCleanCommand {

	private final File archiveDir;
	private final FolderCleanService cleanService;

	public Stream<File> execute() throws ArchivingCleanFaildException {

		File[] existingfiles = archiveDir.listFiles();
		List<FileDeleteState> removedOldArchives = cleanService
				.cleanFolders(Arrays.asList(existingfiles).parallelStream()).collect(Collectors.toList());

		if (removedOldArchives.parallelStream().filter(e -> e.getFaildReasons().isPresent()).count() > 0) {
			throw new ArchivingCleanFaildException();
		} else {
			return removedOldArchives.parallelStream().map(e -> e.getFile());
		}

	}

}
