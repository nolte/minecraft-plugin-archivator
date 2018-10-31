package de.noltarium.minecraft.backup.model;

import java.io.File;
import java.util.Optional;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Builder
@Data
public class BackupCopyModel {
	@Default
	private Optional<Throwable> faildReason = Optional.empty();
	private File original;
	private File target;
}
