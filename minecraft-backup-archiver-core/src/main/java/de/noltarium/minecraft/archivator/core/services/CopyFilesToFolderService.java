package de.noltarium.minecraft.archivator.core.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import de.noltarium.minecraft.archivator.core.model.BackupCopyModel;
import lombok.extern.java.Log;

@Log
public class CopyFilesToFolderService {

	public Stream<BackupCopyModel> copyFilesToFolder(Stream<File> files, File backupWorkingRunDir) {
		if (!backupWorkingRunDir.exists())
			throw new IllegalArgumentException("Mustbe already exists directory");
		else if (!backupWorkingRunDir.isDirectory()) {
			throw new IllegalArgumentException("Mustbe a directory");
		}

		return files.map(original -> {
			File targetFile = Paths.get(backupWorkingRunDir.getAbsolutePath(), original.getName()).toFile();
			BackupCopyModel model = new BackupCopyModel(original, targetFile);
			try {
				targetFile.mkdirs();
				FileUtils.copyDirectory(original, targetFile);
			} catch (IOException e) {
				model.setFaildReason(Optional.of(e));
				log.severe("faild to copy: " + original.getPath() + " to " + targetFile.getPath());
			}

			return model;
		});
	}

}
