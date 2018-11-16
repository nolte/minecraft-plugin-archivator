package de.noltarium.minecraft.archivator.core.backup.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.noltarium.minecraft.archivator.core.services.FilesCompressService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupArchiveCommand {

	private final FilesCompressService compressService;
	private final ArchiveType archivingType;
	private final File archivingBaseDir;

	public File execute(Stream<File> files, String backupId) throws IOException {
		File backupWorkingRunDir = Paths.get(archivingBaseDir.getPath(), createArchiveName(backupId)).toFile();
		return compressService.compressFiles(files.collect(Collectors.toList()), backupWorkingRunDir, archivingType);
	}

	private String createArchiveName(String backupId) {
		return backupId + "." + archivingType.getExtention();
	}

}
