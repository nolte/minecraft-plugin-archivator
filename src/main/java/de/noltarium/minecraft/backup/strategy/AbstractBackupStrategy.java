package de.noltarium.minecraft.backup.strategy;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.NEWLINE;
import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.humanReadableDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.noltarium.minecraft.Archivator;
import de.noltarium.minecraft.backup.steps.ArchiveBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;
import de.noltarium.minecraft.database.model.BackupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

@AllArgsConstructor
@Data
public abstract class AbstractBackupStrategy<T extends ArchiveBaseFolderPreparation> {

	private final T folderPreparation;
	private final ArchivingStep archiving;
	private final BackupEntity backup;

	public AbstractBackupStrategy(BackupEntity backup, ArchivingStep archiving, T folderPreparation) {
		super();
		this.backup = backup;
		this.folderPreparation = folderPreparation;
		this.archiving = archiving;
	}

	public void executeProcess() {
		backup.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
		// prepare the backup filesystem structrue
		folderPreparation.execute();

		List<File> filesForArchive = execute();
		File archive = null;
		try {
			archive = archiving.archiveFiles(filesForArchive, backup.getBackupRunId());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		finalyProcessStep();

		try {
			folderPreparation.cleanupOldArchives();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		backup.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
		backup.setBackupFile(archive.getName());
		// Create the Report
		StringBuffer report = new StringBuffer();
		writeReportHeadline(backup.getStartTime(), backup.getEndTime(), report);

		report.append(createProcessReport().toString());
		report.append(archiving.createReport().toString());

		File reportFile = java.nio.file.Paths.get(folderPreparation.getArchiveBase().toFile().getAbsolutePath(),
				backup.getBackupRunId() + "-report.md").toFile();
		try {
			FileUtils.writeStringToFile(reportFile, report.toString(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			Archivator.getPlugin().getLogger().severe("Faild to write the ReportFile: " + e.getMessage());
		}

	}

	protected abstract void finalyProcessStep();

	protected abstract List<File> execute();

	protected abstract StringBuffer createProcessReport();

	private void writeReportHeadline(OffsetDateTime startTime, OffsetDateTime endTime, StringBuffer report) {

		report.append(
				new Heading("Backup from: " + humanReadableDateFormat.format(Date.from(startTime.toInstant())), 1))
				.append(NEWLINE).append(NEWLINE);

		report.append(new BoldText("From:")).append(" ")
				.append(new ItalicText(humanReadableDateFormat.format(Date.from(startTime.toInstant()))))
				.append(new Text(" - "))
				.append(new ItalicText(humanReadableDateFormat.format(Date.from(endTime.toInstant())))).append(NEWLINE);

		report.append(new BoldText("Files under Backup:")).append(NEWLINE);
		report.append(new UnorderedList<>(backup.getPlanedBackupFiles())).append(NEWLINE).append(NEWLINE);

	}

}
