package de.noltarium.minecraft.archivator.core.backup.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.archivator.core.backup.commands.BackupPrepareCommand;
import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;
import de.noltarium.minecraft.archivator.core.services.BackupFilesCollectorService;

public class BackupPrepareCommandTest {

	private static final Stream PARALLEL_STREAM = new ArrayList().parallelStream();
	private BackupPrepareCommand command;

	@Before
	public void setUp() throws MissingBackupFilesException {
		BackupFilesCollectorService mock = mock(BackupFilesCollectorService.class);
		when(mock.collectPossibleBackupFiles()).thenReturn(PARALLEL_STREAM);
		command = new BackupPrepareCommand(mock);
	}

	@Test
	public void collectingBackupFiles() throws MissingBackupFilesException {
		Stream<File> files = command.execute("abc");
		assertNotNull(files);
		assertThat(files, is(PARALLEL_STREAM));
	}
}
