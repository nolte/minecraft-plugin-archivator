package de.noltarium.minecraft.backup.steps;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;

import java.nio.file.Path;

import lombok.Data;
import lombok.NonNull;

@Data
public class ArchiveTempBaseFolderPreparation extends ArchiveBaseFolderPreparation {

	@NonNull
	private final Path tempBaseDir;

	public ArchiveTempBaseFolderPreparation(Path archiveBase, Path tempBaseDir) {
		super(archiveBase);
		this.tempBaseDir = tempBaseDir;
	}

	@Override
	public void execute() {
		super.execute();
		createFolderIfNotExists(tempBaseDir);
	}
}
