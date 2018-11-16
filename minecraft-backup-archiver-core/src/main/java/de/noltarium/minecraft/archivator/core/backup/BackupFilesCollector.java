package de.noltarium.minecraft.archivator.core.backup;

import java.io.File;
import java.util.List;

public interface BackupFilesCollector {

	List<File> loadFiles();

}
