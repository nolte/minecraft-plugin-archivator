package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.NEWLINE;
import static de.noltarium.minecraft.utils.FolderUtil.humanReadableByteCount;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.noltarium.minecraft.Archivator;
import de.noltarium.minecraft.utils.ArchiveType;
import de.noltarium.minecraft.utils.FileCompress;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class ArchivingStep {

	private final ArchiveType archive_type;

	private File archive;

	private final ArchiveBaseFolderPreparation archiveBase;

	public ArchivingStep(ArchiveBaseFolderPreparation archiveBase, ArchiveType archive_type) {
		this.archiveBase = archiveBase;
		this.archive_type = archive_type;
	}

	public void archiveFiles(List<File> files, String archiveId) throws IOException {

		String destZipFile = archiveBase.getArchiveBase().toString() + "/" + archiveId + "."
				+ archive_type.getExtention();
		Archivator.getPlugin().getLogger().info("Creating backup Archive" + destZipFile);

		// Crate a archive
		Archivator.getPlugin().getLogger().info("Creating backup Archive" + files.size());
		FileCompress compressor = new FileCompress();
		File archivedBackup = new File(destZipFile);
		archive = compressor.compressFiles(files, archivedBackup, archive_type);

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
