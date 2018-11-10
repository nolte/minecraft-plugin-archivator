package de.noltarium.minecraft.backup.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.backup.steps.ArchiveFolderCleanService.FileDeleteState;

public class ArchiveFolderCleanServiceTest {

	private ArchiveFolderCleanService preperator;

	@Before
	public void setUp() {
		preperator = new ArchiveFolderCleanService(3);
	}

	@Test
	public void cleanupArchive() throws Exception {

		List<File> existingFiles = new ArrayList<>();
		File archiveOneReport = new File("/archive/archive-1.md");
		existingFiles.add(archiveOneReport);
		File archiveFileOne = new File("/archive/archive-1.zip");
		existingFiles.add(archiveFileOne);
		File archiveFileTwo = new File("/archive/archive-2.tar.gz");
		existingFiles.add(archiveFileTwo);

		List<FileDeleteState> remoedFiles = preperator.cleanFolders(existingFiles.parallelStream())
				.collect(Collectors.toList());
		assertThat(remoedFiles, hasSize(equalTo(0)));

	}

	@Test
	public void deleteFileFromFS() throws Exception {

		List<File> existingFiles = new ArrayList<>();
		File archiveOneReport = new File("/archive/archive-1.md");
		existingFiles.add(archiveOneReport);
		File archiveFileOne = new File("/archive/archive-1.zip");
		existingFiles.add(archiveFileOne);
		File archiveFileTwo = new File("/archive/archive-2.tar.gz");
		existingFiles.add(archiveFileTwo);
		List<FileDeleteState> remoedFiles = existingFiles.parallelStream().map(preperator.deleteFileFromFS)
				.collect(Collectors.toList());

		assertThat(remoedFiles, hasSize(equalTo(3)));
	}

//	@Test
//	public void onlyLastElementsOfStream() throws Exception {
//		List<File> existingFiles = new ArrayList<>();
//		File archiveOneReport = new File("/archive/archive-1.md");
//		existingFiles.add(archiveOneReport);
//		File archiveFileOne = new File("/archive/archive-1.zip");
//		existingFiles.add(archiveFileOne);
//		File archiveFileTwo = new File("/archive/archive-2.tar.gz");
//		existingFiles.add(archiveFileTwo);
//		List<File> shortedList = preperator.onlyLastPartOfStream(existingFiles.parallelStream(), 2)
//				.collect(Collectors.toList());
//
//		assertThat(shortedList, hasSize(equalTo(1)));
//		assertThat(shortedList, contains(archiveFileTwo));
//
//	}

	@Test
	public void filterTarsAndZipsFromBackendFolder() throws Exception {

		List<File> existingFiles = new ArrayList<>();
		File archiveOneReport = new File("/archive/archive-1.md");
		existingFiles.add(archiveOneReport);
		File archiveFileOne = new File("/archive/archive-1.zip");
		existingFiles.add(archiveFileOne);
		File archiveFileTwo = new File("/archive/archive-2.tar.gz");
		existingFiles.add(archiveFileTwo);

		List<File> archives = existingFiles.parallelStream().filter(preperator.archiveExtentionPredicate)
				.collect(Collectors.toList());
		assertThat(archives, contains(archiveFileOne, archiveFileTwo));
		assertThat(archives, hasSize(equalTo(2)));
		assertThat(archives, not(contains(archiveOneReport)));
	}

	@Test
	public void sortFileStreamByLastModificationOldestFirst() throws Exception {

		List<File> existingFiles = new ArrayList<>();
		File latestFile = mock(File.class);
		when(latestFile.lastModified()).thenReturn(5000l);
		existingFiles.add(latestFile);
		File middleFile = mock(File.class);
		when(middleFile.lastModified()).thenReturn(4000l);
		existingFiles.add(middleFile);
		File oldest = mock(File.class);
		when(oldest.lastModified()).thenReturn(3000l);
		existingFiles.add(oldest);

		List<File> sortedList = existingFiles.stream().sorted(preperator.fileComparingOldestFirst)
				.collect(Collectors.toList());

		assertThat(sortedList, hasSize(equalTo(3)));
		assertThat(sortedList, contains(latestFile, middleFile, oldest));

	}

}
