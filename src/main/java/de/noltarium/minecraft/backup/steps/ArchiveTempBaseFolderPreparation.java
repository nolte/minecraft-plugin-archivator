package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;

import java.nio.file.Path;
import java.util.Optional;

import lombok.Data;
import lombok.NonNull;

@Data
public class ArchiveTempBaseFolderPreparation extends ArchiveBaseFolderPreparation {

	@NonNull
	private final Path tempBaseDir;

	public ArchiveTempBaseFolderPreparation(Path archiveBase, Path tempBaseDir,
			Optional<ArchiveFolderCleanService> cleanService) {
		super(archiveBase, cleanService);
		this.tempBaseDir = tempBaseDir;
	}

	@Override
	public void execute() {
		super.execute();
		createFolderIfNotExists(tempBaseDir);
	}
}
