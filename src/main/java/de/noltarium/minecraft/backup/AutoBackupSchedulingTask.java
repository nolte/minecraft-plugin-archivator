package de.noltarium.minecraft.backup;

import org.bukkit.scheduler.BukkitRunnable;

import de.noltarium.minecraft.backup.model.BackupAlwaysRunningException;

public class AutoBackupSchedulingTask extends BukkitRunnable {

	private BackupService backupService;

	public AutoBackupSchedulingTask(BackupService backupService) {
		this.backupService = backupService;
	}

	@Override
	public void run() {
		try {
			backupService.startFullBackup();
		} catch (BackupAlwaysRunningException e) {
			throw new RuntimeException(e);
		}

	}

}
