package de.noltarium.minecraft.archivator.core.backup.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupSaftyPrepareCommand;
import de.noltarium.minecraft.archivator.core.backup.commands.BackupSaftyPrepareCommand.FaildToCopyFilesException;
import de.noltarium.minecraft.archivator.core.model.BackupCopyModel;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;
import de.noltarium.minecraft.archivator.core.services.CopyFilesToFolderService;

public class BackupSaftyPrepareCommandTest {

	private static final Stream PARALLEL_STREAM = new ArrayList().parallelStream();
	private BackupSaftyPrepareCommand command;
	private CopyFilesToFolderService copyFilesServiceMock;

	@Before
	public void setUp() throws Exception {
		BackupFilesCollectorService mock = mock(BackupFilesCollectorService.class);
		when(mock.collectPossibleBackupFiles()).thenReturn(PARALLEL_STREAM);
		copyFilesServiceMock = mock(CopyFilesToFolderService.class);
		command = new BackupSaftyPrepareCommand(mock(File.class), copyFilesServiceMock, mock);
	}

	@Test
	public void collectingBackupFiles() throws Exception {
		List<BackupCopyModel> copyResultList = new ArrayList<>();
		BackupCopyModel model = new BackupCopyModel(mock(File.class), mock(File.class));
		copyResultList.add(model);
		when(copyFilesServiceMock.copyFilesToFolder(Mockito.any(), Mockito.any()))
				.thenReturn(copyResultList.parallelStream());

		Stream<File> files = command.execute("abc");
		assertNotNull(files);
		assertThat(files, not(PARALLEL_STREAM));
		assertThat(files.count(), is(1l));
	}

	@Test
	public void collectingBackupEmptyListFiles() throws Exception {
		List<BackupCopyModel> copyResultList = new ArrayList<>();
		when(copyFilesServiceMock.copyFilesToFolder(Mockito.any(), Mockito.any()))
				.thenReturn(copyResultList.parallelStream());

		Stream<File> files = command.execute("abc");
		assertNotNull(files);
		assertThat(files, not(PARALLEL_STREAM));
		assertThat(files.count(), is(0l));
	}

	@Test(expected = FaildToCopyFilesException.class)
	public void somFaildsInCopyProcess() throws Exception {
		List<BackupCopyModel> copyResultList = new ArrayList<>();
		BackupCopyModel model = new BackupCopyModel(null, null);
		model.setFaildReason(Optional.of(new IOException()));
		copyResultList.add(model);
		when(copyFilesServiceMock.copyFilesToFolder(Mockito.any(), Mockito.any()))
				.thenReturn(copyResultList.parallelStream());
		command.execute("abc");
	}

}
