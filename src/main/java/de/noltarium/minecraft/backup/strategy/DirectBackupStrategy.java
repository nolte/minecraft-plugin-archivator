package de.noltarium.minecraft.backup.strategy;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import de.noltarium.minecraft.backup.steps.ArchiveBaseFolderPreparation;
import de.noltarium.minecraft.backup.steps.ArchivingStep;

public class DirectBackupStrategy extends AbstractBackupStrategy<ArchiveBaseFolderPreparation> {

	public DirectBackupStrategy(String archiveId, List<File> backupSources, ArchivingStep archiving,
			ArchiveBaseFolderPreparation folderPreparation) {
		super(archiveId, backupSources, archiving, folderPreparation);
	}

	@Override
	public List<File> execute() {
		// do nothink return the original
		return getBackupSources();
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
