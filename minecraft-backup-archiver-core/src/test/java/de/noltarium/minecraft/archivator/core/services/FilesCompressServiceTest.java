package de.noltarium.minecraft.archivator.core.services;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;

/**
 * The FileCompress is not realy mockable ... so do a small IT Test
 * 
 * @author nolte
 *
 */
public class FilesCompressServiceTest {

	private FilesCompressService compressService;
	private File targetDir;
	private File fileTwo;
	private File fileOne;
	private String baseTestDir;
	private File fileThreeAFolder;

	@Before
	public void setUp() throws FileNotFoundException, UnsupportedEncodingException {
		String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		targetDir = new File(relPath + "../../target/testdata");
		if (!targetDir.exists()) {
			targetDir.mkdir();
		}
		baseTestDir = relPath + "../../target/testdata";

		// create test files for archiving
		PrintWriter writer = new PrintWriter(baseTestDir + "/the-file-name.txt", "UTF-8");
		writer.println("The first line");
		writer.println("The second line");
		writer.close();
		fileOne = new File(baseTestDir + "/the-file-name.txt");

		PrintWriter writerSecond = new PrintWriter(baseTestDir + "/the-second.txt", "UTF-8");
		writerSecond.println("The first line");
		writerSecond.println("The second line");
		writerSecond.close();

		fileTwo = new File(baseTestDir + "the-second.txt");

		fileThreeAFolder = new File(baseTestDir + "/sometestfolder");
		if (!fileThreeAFolder.exists()) {
			fileThreeAFolder.mkdir();
		}

		compressService = new FilesCompressService();

	}

	@After
	public void cleanUp() throws IOException {
		FileUtils.forceDelete(targetDir);
	}

	@Test
	public void createArchiveOfFilesToTar() throws Exception {
		File archiveFile = new File(baseTestDir + "/archive.tar.gz");
		File archived = compressService.compressFiles(Arrays.asList(fileOne, fileTwo, fileThreeAFolder), archiveFile,
				ArchiveType.TAR);
		assertNotNull(archived);
	}

	@Test
	public void createArchiveOfFilesToZip() throws Exception {
		File archiveFile = new File(baseTestDir + "/archive.zip");
		File archived = compressService.compressFiles(Arrays.asList(fileOne, fileTwo, fileThreeAFolder), archiveFile,
				ArchiveType.ZIP);
		assertNotNull(archived);
	}

}
