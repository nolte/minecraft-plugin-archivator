package de.noltarium.minecraft.archivator.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.noltarium.minecraft.archivator.core.backup.BackupProcessService;
import de.noltarium.minecraft.archivator.core.model.BackupAlwaysRunningException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BackupPluginCommandExecutor implements CommandExecutor {

	private final JavaPlugin plugin;

	private final BackupProcessService service;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		plugin.getLogger().info("Start Backup");
		try {
			String backupId = service.startBackup();
			plugin.getLogger().info("Backup started: " + backupId);
		} catch (BackupAlwaysRunningException e) {
			plugin.getLogger().warning("the backup process is allways running try it later");
		}

		return false;
	}

}
