package de.noltarium.minecraft.archivator.core.backup.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCleanCommand;
import de.noltarium.minecraft.archivator.core.model.ArchivingCleanFaildException;
import de.noltarium.minecraft.archivator.core.services.FolderCleanService;
import de.noltarium.minecraft.archivator.core.services.FolderCleanService.FileDeleteState;

public class BackupArchiveCleanCommandTest {

	private BackupArchiveCleanCommand command;
	private FolderCleanService cleanServiceMock;
	private File archiveBaseDirMock;

	@Before
	public void setUp() {
		cleanServiceMock = mock(FolderCleanService.class);
		archiveBaseDirMock = mock(File.class);
		File[] exstingArchiveFiles = new File[] { mock(File.class) };
		when(archiveBaseDirMock.listFiles()).thenReturn(exstingArchiveFiles);
		command = new BackupArchiveCleanCommand(archiveBaseDirMock, cleanServiceMock);
	}

	@Test
	public void successfullCleaningArchivedFiles() throws Exception {
		command.execute();
	}

	@Test(expected = ArchivingCleanFaildException.class)
	public void faildCleaningArchivedFiles() throws Exception {
		FileDeleteState mock = mock(FileDeleteState.class);
		when(mock.getFaildReasons()).thenReturn(Optional.of(new IOException()));
		Stream<FileDeleteState> faildStream = (Arrays.asList(mock)).parallelStream();
		when(cleanServiceMock.cleanFolders(Mockito.any())).thenReturn(faildStream);
		command.execute();
	}
}
