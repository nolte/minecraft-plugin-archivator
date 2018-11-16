package de.noltarium.minecraft.archivator.core.services;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public class FolderCleanService {

	final Integer maxReleases;

	public FolderCleanService(Integer maxReleases) {
		this.maxReleases = maxReleases;
	}

	final Function<? super File, ? extends FileDeleteState> deleteFileFromFS = file -> {
		Optional<Exception> faildReason = null;
		boolean removed;
		try {
			FileUtils.forceDelete(file);
			removed = true;
			faildReason = Optional.empty();
		} catch (Exception e) {
			log.severe("faild to delete " + file.getPath());
			faildReason = Optional.of(e);
			removed = false;
		}
		return new FileDeleteState(file, faildReason, removed);
	};

	final Predicate<? super File> archiveExtentionPredicate = e -> (e.getPath().endsWith(ArchiveType.ZIP.getExtention())
			|| e.getPath().endsWith(ArchiveType.TAR.getExtention()));
	final Comparator<File> fileComparingOldestFirst = Comparator.comparingLong(File::lastModified).reversed();

	/**
	 * Remove the oldest folders/files
	 * 
	 * @param archivedFiles
	 * @return
	 */
	public Stream<FileDeleteState> cleanFolders(Stream<File> archivedFiles) {
		return archivedFiles.filter(archiveExtentionPredicate).sorted(fileComparingOldestFirst).skip(maxReleases)
				.map(deleteFileFromFS);
	}

	@Getter
	public class FileDeleteState {
		final File file;
		final Optional<Exception> faildReasons;
		final boolean removed;

		public FileDeleteState(File file, Optional<Exception> faildReasons, boolean removed) {
			this.faildReasons = faildReasons;
			this.file = file;
			this.removed = removed;
		}
	}

}
