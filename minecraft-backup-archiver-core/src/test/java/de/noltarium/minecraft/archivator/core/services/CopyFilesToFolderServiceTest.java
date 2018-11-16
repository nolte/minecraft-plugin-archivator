package de.noltarium.minecraft.archivator.core.services;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.io.FileMatchers.aFileWithCanonicalPath;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.noltarium.minecraft.archivator.core.model.BackupCopyModel;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class CopyFilesToFolderServiceTest {

	@Test
	public void copyTwoFilesOneSuccesFullOneFaild() throws Exception {

		mock(FileUtils.class);

		PowerMockito.spy(FileUtils.class);

		CopyFilesToFolderService copyStep = new CopyFilesToFolderService();

		File backupTarget = mock(File.class);
		when(backupTarget.getPath()).thenReturn("tmpWorkdir");
		when(backupTarget.getAbsolutePath()).thenReturn("/tmp/tmpWorkdir");
		when(backupTarget.isDirectory()).thenReturn(true);
		when(backupTarget.exists()).thenReturn(true);

		List<File> files = new ArrayList<>();
		File worldA = new File("worlda");
		files.add(worldA);
		File worldB = new File("worldb");
		files.add(worldB);

		PowerMockito.doNothing().when(FileUtils.class, "copyDirectory", Mockito.eq(worldA), Mockito.any(File.class));
		IOException faildToCopyWorldB = new IOException("faild to copy");
		PowerMockito.doThrow(faildToCopyWorldB).when(FileUtils.class, "copyDirectory", Mockito.eq(worldB),
				Mockito.any(File.class));

		List<BackupCopyModel> movedFiles = copyStep.copyFilesToFolder(files.parallelStream(), backupTarget)
				.collect(Collectors.toList());

		assertThat(movedFiles, hasSize(equalTo(2)));

		// check the result list, one faild and one Succesfull
		assertThat(movedFiles, allOf(
				hasItem(allOf(Matchers.<BackupCopyModel>hasProperty("original", equalTo(worldA)),
						Matchers.<BackupCopyModel>hasProperty("faildReason", equalTo(Optional.empty())),
						Matchers.<BackupCopyModel>hasProperty("target",
								aFileWithCanonicalPath(containsString("/tmp/tmpWorkdir/worlda"))))),
				hasItem(allOf(Matchers.<BackupCopyModel>hasProperty("original", equalTo(worldB)),
						Matchers.<BackupCopyModel>hasProperty("faildReason", equalTo(Optional.of(faildToCopyWorldB))),
						Matchers.<BackupCopyModel>hasProperty("target",
								aFileWithCanonicalPath(containsString("/tmp/tmpWorkdir/worldb")))))));

	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidTempTheTempIsNotAFolder() throws Exception {

		File tempFileFolder = mock(File.class);
		when(tempFileFolder.isDirectory()).thenReturn(false);
		when(tempFileFolder.exists()).thenReturn(true);
		CopyFilesToFolderService copyStep = new CopyFilesToFolderService();
		copyStep.copyFilesToFolder(new ArrayList<File>().parallelStream(), tempFileFolder);

	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidTempTheTempIsNotExists() throws Exception {

		File tempFileFolder = mock(File.class);
		when(tempFileFolder.exists()).thenReturn(false);
		CopyFilesToFolderService copyStep = new CopyFilesToFolderService();
		copyStep.copyFilesToFolder(new ArrayList<File>().parallelStream(), tempFileFolder);

	}

}
