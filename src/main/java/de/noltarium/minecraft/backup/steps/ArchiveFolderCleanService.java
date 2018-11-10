package de.noltarium.minecraft.backup.steps;

import java.io.File;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.noltarium.minecraft.utils.ArchiveType;
import lombok.Data;
import lombok.extern.java.Log;

@Log
public class ArchiveFolderCleanService {

	final Integer maxReleases;

	public ArchiveFolderCleanService(Integer maxReleases) {
		this.maxReleases = maxReleases;
	}

	final Function<? super File, ? extends FileDeleteState> deleteFileFromFS = file -> {
		Optional<Exception> faildReason = null;
		boolean removed;
		try {
			removed = Files.deleteIfExists(file.toPath());
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

	public Stream<FileDeleteState> cleanFolders(Stream<File> archivedFiles) {
		return archivedFiles.filter(archiveExtentionPredicate).sorted(fileComparingOldestFirst).skip(maxReleases)
				.map(deleteFileFromFS);
	}

	@Data
	class FileDeleteState {
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
