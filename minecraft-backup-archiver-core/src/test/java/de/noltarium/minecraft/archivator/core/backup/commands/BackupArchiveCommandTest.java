package de.noltarium.minecraft.archivator.core.backup.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupArchiveCommand;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;

public class BackupArchiveCommandTest {

	private BackupArchiveCommand command;
	private File archiveMock;

	@Before
	public void setUp() throws Exception {
		File archiveBaseDirMock = new File("/tmp/archives");
		FilesCompressService mock = mock(FilesCompressService.class);
		archiveMock = mock(File.class);
		when(mock.compressFiles(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(archiveMock);
		command = new BackupArchiveCommand(mock, ArchiveType.TAR, archiveBaseDirMock);
	}

	@Test
	public void successFullArchivingFiles() throws Exception {
		File archivedFileMock = mock(File.class);
		when(archivedFileMock.getPath()).thenReturn("/tmp/world_a");
		Stream<File> files = Arrays.asList(archivedFileMock).parallelStream();
		File archive = command.execute(files, "abc");
		assertThat(archive, is(archiveMock));
	}

}
