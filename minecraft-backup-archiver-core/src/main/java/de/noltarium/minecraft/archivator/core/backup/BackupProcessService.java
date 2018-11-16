package de.noltarium.minecraft.archivator.core.backup;

import de.noltarium.minecraft.archivator.core.model.BackupAlwaysRunningException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BackupProcessService {

	private Thread thread = null;

	private final BackupProcessFactory processFactory;

	public String startBackup() throws BackupAlwaysRunningException {

		if (thread != null && thread.isAlive()) {
			throw new BackupAlwaysRunningException();
		} else {
			BackupProcess createBackupProcess = processFactory.createBackupProcess();
			thread = new Thread(createBackupProcess);
			thread.start();
			return createBackupProcess.getBackupId();
		}

	}

}
