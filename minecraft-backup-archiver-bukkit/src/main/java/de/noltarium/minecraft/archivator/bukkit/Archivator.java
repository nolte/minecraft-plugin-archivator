package de.noltarium.minecraft.archivator.bukkit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import de.noltarium.minecraft.archivator.bukkit.chat.ChatFacade;
import de.noltarium.minecraft.archivator.bukkit.commands.PluginCommandConfigurationProxy;
import de.noltarium.minecraft.archivator.core.database.DatabaseMigratorService;
import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;

public final class Archivator extends JavaPlugin {

	private static Archivator plugin;

	private static BukkitConfigFacade configFacade;

	@Override
	public void onEnable() {
		getLogger().info("onEnable has been invoked!");
		plugin = this;
		try {
			// prepare defaults
			PluginInitiatorHelper.initPlugin(plugin);

			ChatFacade chat = new ChatFacade(plugin.getName());

			configFacade = new BukkitConfigFacade(this.getConfig(), this.getDataFolder());

			new DatabaseMigratorService(configFacade.getDataSource(), configFacade.getTablePrefix()).migrateDatabase();

			BackupDAO backupDAO = new BackupDAO(configFacade.getDataSource(), configFacade.getTablePrefix(),
					this.getLogger());

			// configure the plugin commands
			new PluginCommandConfigurationProxy(backupDAO, chat, configFacade).configure(this);

		} catch (InvalidConfigurationException e) {
			throw new RuntimeException("faild init archivator Plugin");
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
		plugin = null;
	}

}
