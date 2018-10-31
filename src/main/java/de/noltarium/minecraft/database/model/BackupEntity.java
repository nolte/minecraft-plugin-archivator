package de.noltarium.minecraft.database.model;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class BackupEntity {

	private String backupRunId;

	private OffsetDateTime startTime;

	private OffsetDateTime endTime;

	private String trigger;

	private String backupFile;

	@Singular
	private List<File> planedBackupFiles;

}
