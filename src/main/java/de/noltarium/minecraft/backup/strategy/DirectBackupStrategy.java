package de.noltarium.minecraft.backup.strategy;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import de.noltarium.minecraft.backup.steps.ArchiveBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;
import de.noltarium.minecraft.database.model.BackupEntity;

public class DirectBackupStrategy extends AbstractBackupStrategy<ArchiveBaseFolderPreparation> {

	public DirectBackupStrategy(BackupEntity backup, ArchivingStep archiving,
			ArchiveBaseFolderPreparation folderPreparation) {
		super(backup, archiving, folderPreparation);
	}

	@Override
	public List<File> execute() {
		// do nothink return the original
		return getBackup().getPlanedBackupFiles();
	}

	@Override
	protected StringBuffer createProcessReport() {
		return new StringBuffer();
	}

	@Override
	protected void finalyProcessStep() {
		// TODO Auto-generated method stub

	}

}
