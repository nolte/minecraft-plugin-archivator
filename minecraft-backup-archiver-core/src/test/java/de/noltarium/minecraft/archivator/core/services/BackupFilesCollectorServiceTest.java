package de.noltarium.minecraft.archivator.core.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.archivator.core.backup.BackupFilesCollector;
import de.noltarium.minecraft.archivator.core.model.MissingBackupFilesException;

public class BackupFilesCollectorServiceTest {

	private BackupFilesCollector filesCollectorMock;
	private BackupFilesCollectorService service;

	@Before
	public void setUp() {
		List<BackupFilesCollector> collectorList = new ArrayList<>();
		filesCollectorMock = mock(BackupFilesCollector.class);
		collectorList.add(filesCollectorMock);
		service = new BackupFilesCollectorService(collectorList);
	}

	@Test(expected = MissingBackupFilesException.class)
	public void collectEmptyFileListFromCollector() throws Exception {
		when(filesCollectorMock.loadFiles()).thenReturn(new ArrayList<>());
		service.collectPossibleBackupFiles();

	}

	@Test()
	public void collectFileListFromCollector() throws Exception {
		ArrayList<File> value = new ArrayList<>();
		value.add(mock(File.class));
		when(filesCollectorMock.loadFiles()).thenReturn(value);
		Stream<File> collectedFiles = service.collectPossibleBackupFiles();
		assertNotNull(collectedFiles);
		assertThat(collectedFiles.count(), is(1l));
	}

}
