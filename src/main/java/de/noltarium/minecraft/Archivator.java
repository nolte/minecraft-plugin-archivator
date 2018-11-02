package de.noltarium.minecraft;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.NEWLINE;
import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.humanReadableDateFormat;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.noltarium.minecraft.backup.BackupService;
import de.noltarium.minecraft.backup.model.BackupAlwaysRunningException;
import de.noltarium.minecraft.chat.ChatFacade;
import de.noltarium.minecraft.config.ArchivatorConfigurationFacade;
import de.noltarium.minecraft.config.DatabaseHandler;
import de.noltarium.minecraft.database.DatabaseFacade;
import de.noltarium.minecraft.database.DatabaseMigratorService;
import de.noltarium.minecraft.database.model.BackupEntity;

public final class Archivator extends JavaPlugin {

	private static Archivator plugin;

	private static ArchivatorConfigurationFacade configFacade;
	private static ChatFacade chatFacade;
	private static BackupService backupService;

	private DatabaseFacade databaseFacade;
//	private static BukkitTask autoBackupTasks;

	@Override
	public void onEnable() {
		getLogger().info("onEnable has been invoked!");
		plugin = this;
		try {
			configFacade = new ArchivatorConfigurationFacade(this);
			chatFacade = new ChatFacade(this.getName());

			DatabaseHandler handler = configFacade.getDatabaseHandler();
			new DatabaseMigratorService(handler).migrateDatabase();
			databaseFacade = new DatabaseFacade(handler, getLogger());

			backupService = new BackupService(configFacade.getBackupConfigProvider(), chatFacade, databaseFacade);

		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			getLogger().severe("Fail to init the config");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
		plugin = null;
		if (backupService != null) {
			backupService.shutdown();
			backupService = null;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch (cmd.getName().toLowerCase()) {
		case "backup":
			try {
				if (sender instanceof Player)
					chatFacade.sendMessage((Player) sender, "The Backup has been planed");

				backupService.startFullBackup();
			} catch (BackupAlwaysRunningException e) {
				if (sender instanceof Player)
					chatFacade.sendMessage((Player) sender,
							ChatColor.RED + "Faild," + ChatColor.BLACK + " Backup allways running");
				return false;
			}
			break;
		case "backupinfo":
			if (sender instanceof Player) {
				Optional<OffsetDateTime> lastSuccessfull = databaseFacade.loadLastSuccessfullRun();
				String format = "";
				StringBuffer backupResult = new StringBuffer();
				if (lastSuccessfull.isPresent()) {
					OffsetDateTime offsetDateTime = lastSuccessfull.get();
					format = offsetDateTime.format(humanReadableDateFormat);
				} else {
					format = ChatColor.RED + "NEVER !!";
				}
				backupResult.append("Last Successfull Backup: ").append(format).append(NEWLINE);
				chatFacade.sendMessage((Player) sender, backupResult.toString());
			}
			break;
		case "backuplist":
			if (sender instanceof Player) {
				databaseFacade.updateLastSuccessFullRun(OffsetDateTime.now(ZoneOffset.UTC));
				List<String> backups = backupService.findExistingBackups();

				StringBuffer backupResult = new StringBuffer();
				backupResult.append("Existing Backups: ").append(NEWLINE);

				for (String string : backups) {
					backupResult.append(ChatColor.GOLD + " *  " + ChatColor.WHITE + string).append(NEWLINE);
				}

				chatFacade.sendMessage((Player) sender, backupResult.toString());

				List<BackupEntity> backupsFromDB = databaseFacade.loadBackupRuns();
				backupResult = new StringBuffer();
				backupResult.append("Existing Backups: ").append(NEWLINE);

				for (BackupEntity string : backupsFromDB) {
					backupResult.append(ChatColor.GOLD + " *  " + ChatColor.WHITE + string.getBackupFile()
							+ ChatColor.GRAY + "("
							+ string.getStartTime().format(ArchivatorConfigurationFacade.humanReadableDateFormat)
							+ " runs: " + string.getEndTime()
									.minus(string.getStartTime().toEpochSecond(), ChronoUnit.SECONDS).toEpochSecond()
							+ ")").append(NEWLINE);
				}

				chatFacade.sendMessage((Player) sender, backupResult.toString());
			}

			break;

		default:
			return false;
		}
		return true;
	}

	@EventHandler
	public void onCommandSend(ServerCommandEvent event) {
		String command = event.getCommand();
		if (command.equalsIgnoreCase("foba")) {
			getLogger().info("soome server command ....");
		}
	}

	public static synchronized Archivator getPlugin() {
		return plugin;
	}

	public static ChatFacade getChat() {
		return chatFacade;
	}

	public static ArchivatorConfigurationFacade getConfigFacade() {
		return configFacade;
	}
}
