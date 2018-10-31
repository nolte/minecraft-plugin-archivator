package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
public class ArchiveBaseFolderPreparation implements BackupStep {

	@NonNull
	final Path archiveBase;

	@Override
	public void execute() {
		createFolderIfNotExists(archiveBase);
	}

}
