package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.NEWLINE;
import static de.noltarium.minecraft.utils.FolderUtil.humanReadableByteCount;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.noltarium.minecraft.utils.ArchiveType;
import de.noltarium.minecraft.utils.FileCompress;
import lombok.extern.java.Log;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

@Log
public class ArchivingStep {

	private final ArchiveType archive_type;

	private final FileCompress compressor;

	private File archive;

	public ArchivingStep(ArchiveType archive_type, FileCompress compressorService) {
		this.archive_type = archive_type;
		this.compressor = compressorService;
	}

	public File archiveFiles(List<File> files, File archivedFolder, String archiveId) throws IOException {
		if (archivedFolder.isDirectory()) {
			String destZipFile = archivedFolder.getPath() + "/" + archiveId + "." + archive_type.getExtention();
			return archiveFiles(files, new File(destZipFile));
		} else {
			throw new IllegalArgumentException("The ArchiveFolder is not a directory:" + archivedFolder.getPath());
		}
	}

	public File archiveFiles(List<File> files, File archivedBackup) throws IOException {

		archive = compressor.compressFiles(files, archivedBackup, archive_type);
		return archive;

	}

	public StringBuffer createReport() {
		StringBuffer report = new StringBuffer();

		report.append(NEWLINE).append(new Heading("Archive Information", 2)).append(NEWLINE).append(NEWLINE);

		report.append(new BoldText("Archive:")).append(" ").append(new ItalicText(archive.getPath())).append(NEWLINE);
		report.append(new BoldText("Size: ")).append(new ItalicText(humanReadableByteCount(archive.length(), true)))
				.append(NEWLINE);

		return report;
	}

}
