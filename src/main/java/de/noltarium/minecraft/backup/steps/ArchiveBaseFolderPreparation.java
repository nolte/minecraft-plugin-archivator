package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	final Integer maxReleases;

	@Override
	public void execute() {
		createFolderIfNotExists(archiveBase);
	}

	public List<File> cleanupOldArchives() throws IOException {
		log.info("Start cleaning archive folder");
		List<File> cleanedFiles = new ArrayList<>();
		if (maxReleases != null && maxReleases > 0) {
			List<File> backups = Arrays.asList(archiveBase.toFile().listFiles()).parallelStream()
					.filter(e -> !e.getPath().endsWith(".md")).sorted(Comparator.comparingLong(File::lastModified))
					.collect(Collectors.toList());

			File[] files = archiveBase.toFile().listFiles();
			Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

			int backupDiff = backups.size() - maxReleases;
			log.info("existing backups:" + backups.size());
			if (backupDiff > 0)
				for (int i = 0; i < backupDiff; i++) {
					File file = backups.get(i);
					log.info("Remove: " + file.getPath());
					Files.delete(file.toPath());
					cleanedFiles.add(file);
				}

		}

		return cleanedFiles;
	}

}
