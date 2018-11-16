package de.noltarium.minecraft.archivator.core.model;

import java.io.File;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class BackupCopyModel {

	private final File original;
	private final File target;

	@Setter
	private Optional<Throwable> faildReason = Optional.empty();
}
