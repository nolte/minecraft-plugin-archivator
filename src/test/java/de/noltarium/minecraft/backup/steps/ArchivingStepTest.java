package de.noltarium.minecraft.backup.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import de.noltarium.minecraft.utils.ArchiveType;
import de.noltarium.minecraft.utils.FileCompress;

public class ArchivingStepTest {

	private ArchivingStep step;

	@Before
	public void setUp() throws IOException {
		FileCompress compressorService = mock(FileCompress.class);
		when(compressorService.compressFiles(ArgumentMatchers.<List<File>>any(), any(File.class),
				any(ArchiveType.class))).thenReturn(new File("first-backup.tar.gz"));
		step = new ArchivingStep(ArchiveType.TAR, compressorService);

	}

	@Test
	public void createSomeArchiveFromAListOfFiles() throws IOException {

		File archiveFolder = mock(File.class);
		when(archiveFolder.isDirectory()).thenReturn(true);

		ArrayList<File> files = new ArrayList<>();
		files.add(mock(File.class));
		files.add(mock(File.class));
		File archive = step.archiveFiles(files, archiveFolder, "first-backup");
		StringBuffer report = step.createReport();
		System.out.println(report.toString());
		assertThat(archive, is(archive));
		assertNotNull(report);
	}

	@Test(expected = IllegalArgumentException.class)
	public void theArchiveTargetIsNotDirectory() throws IOException {

		File archiveFolder = mock(File.class);
		when(archiveFolder.isDirectory()).thenReturn(false);
		step.archiveFiles(new ArrayList<>(), archiveFolder, "first-backup");

	}
}
