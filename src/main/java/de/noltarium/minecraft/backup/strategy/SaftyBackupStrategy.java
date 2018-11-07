package de.noltarium.minecraft.backup.strategy;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.NEWLINE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import de.noltarium.minecraft.backup.model.BackupCopyModel;
import de.noltarium.minecraft.backup.model.BackupCopyModel.BackupCopyModelBuilder;
import de.noltarium.minecraft.backup.steps.ArchiveTempBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;
import de.noltarium.minecraft.database.model.BackupEntity;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class SaftyBackupStrategy extends AbstractBackupStrategy<ArchiveTempBaseFolderPreparation> {

	private List<BackupCopyModel> preparedElements;
	private File backupWorkingRunDir;

	public SaftyBackupStrategy(BackupEntity backup, ArchivingStep archiving,
			ArchiveTempBaseFolderPreparation folderPreparation) {
		super(backup, archiving, folderPreparation);
	}

	@Override
	protected List<File> execute() {
		String backupWorkingBasdir = getFolderPreparation().getTempBaseDir().toFile().getAbsolutePath();

		backupWorkingRunDir = Paths.get(backupWorkingBasdir, getBackup().getBackupRunId()).toFile();
		backupWorkingRunDir.mkdirs();
		preparedElements = getBackup().getPlanedBackupFiles().parallelStream().map(original -> {
			File targetFile = Paths.get(backupWorkingRunDir.getAbsolutePath(), original.getName()).toFile();
			BackupCopyModelBuilder builder = BackupCopyModel.builder();
			builder.original(original).target(targetFile);
			try {
				targetFile.mkdirs();
				FileUtils.copyDirectory(original, targetFile);
			} catch (IOException e) {
				builder.faildReason(Optional.of(e));
				e.printStackTrace();
			}
			return builder.build();
		}).collect(Collectors.toList());

		return preparedElements.parallelStream().map(e -> e.getTarget()).collect(Collectors.toList());

	}

	@Override
	protected StringBuffer createProcessReport() {
		StringBuffer report = new StringBuffer();

		report.append(NEWLINE).append(new Heading("Prepared files", 2)).append(NEWLINE).append(NEWLINE);

		Table.Builder tableBuilder = new Table.Builder()
				.withAlignments(Table.ALIGN_CENTER, Table.ALIGN_LEFT, Table.ALIGN_LEFT)
				.withRowLimit(preparedElements.size() + 1).addRow("Status", "Source", "Destination");

		for (BackupCopyModel backupCopyModel : preparedElements) {
			tableBuilder.addRow(!backupCopyModel.getFaildReason().isPresent(), backupCopyModel.getOriginal().getPath(),
					backupCopyModel.getTarget().getPath());

		}
		report.append(tableBuilder.build()).append(NEWLINE);

		return report;
	}

	@Override
	protected void finalyProcessStep() {
		if (backupWorkingRunDir.exists())
			try {
				FileUtils.deleteDirectory(backupWorkingRunDir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

	}

}
