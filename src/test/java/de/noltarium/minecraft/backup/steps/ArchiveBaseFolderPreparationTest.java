package de.noltarium.minecraft.backup.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import de.noltarium.minecraft.backup.steps.ArchiveFolderCleanService.FileDeleteState;

public class ArchiveBaseFolderPreparationTest {

	@Test
	public void cleaningArchiveNoCleaningService() throws Exception {
		ArchiveBaseFolderPreparation archiveB = new ArchiveBaseFolderPreparation(mock(Path.class), Optional.empty());
		List<File> cleanedArchive = archiveB.cleanupOldArchives();
		assertThat(cleanedArchive, hasSize(equalTo(0)));
	}

	@Test
	public void cleaningArchiveWithCleaningService() throws Exception {
		ArchiveFolderCleanService cleningService = mock(ArchiveFolderCleanService.class);

		ArrayList<FileDeleteState> list = new ArrayList<>();
		when(cleningService.cleanFolders(ArgumentMatchers.<Stream<File>>any())).thenReturn(list.stream());

		Path archivePathMock = mock(Path.class);
		File archiveFolderMock = mock(File.class);
		when(archivePathMock.toFile()).thenReturn(archiveFolderMock);
		ArrayList<File> existingArchivedFiles = new ArrayList<File>();
		when(archiveFolderMock.listFiles())
				.thenReturn(existingArchivedFiles.toArray(new File[existingArchivedFiles.size()]));

		ArchiveBaseFolderPreparation archiveB = new ArchiveBaseFolderPreparation(archivePathMock,
				Optional.of(cleningService));
		List<File> cleanedArchive = archiveB.cleanupOldArchives();
		assertThat(cleanedArchive, hasSize(equalTo(0)));
	}

}
