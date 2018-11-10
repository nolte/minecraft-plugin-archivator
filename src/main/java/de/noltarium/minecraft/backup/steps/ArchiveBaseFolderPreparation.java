package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.noltarium.minecraft.backup.steps.ArchiveFolderCleanService.FileDeleteState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

@AllArgsConstructor
@Data
@Log
public class ArchiveBaseFolderPreparation implements BackupStep {

	@NonNull
	final Path archiveBase;

	final Optional<ArchiveFolderCleanService> cleanService;

	@Override
	public void execute() {
		createFolderIfNotExists(archiveBase);
	}

	public List<File> cleanupOldArchives() throws IOException {
		log.info("Start cleaning archive folder");
		List<File> cleanedFiles = new ArrayList<>();
		if (cleanService.isPresent()) {
			// find all files in the archive
			List<File> asList = Arrays.asList(archiveBase.toFile().listFiles());

			// remove old archived files
			List<FileDeleteState> cleaned = cleanService.get().cleanFolders(asList.parallelStream())
					.collect(Collectors.toList());

			// success full removed
			cleanedFiles = cleaned.parallelStream().filter(file -> file.isRemoved()).map(file -> file.getFile())
					.collect(Collectors.toList());

			// TODO Mark the removed archives at the database

		}

		return cleanedFiles;
	}

}
