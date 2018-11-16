package de.noltarium.minecraft.archivator.core.database.model;

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
	private OffsetDateTime createdAt;

	private String trigger;

	private String backupFile;
	private Long backupFileSize;

	private OffsetDateTime fsRemovedAt;
	private String fsRemovedBy;

	@Singular
	private List<File> planedBackupFiles;

}
