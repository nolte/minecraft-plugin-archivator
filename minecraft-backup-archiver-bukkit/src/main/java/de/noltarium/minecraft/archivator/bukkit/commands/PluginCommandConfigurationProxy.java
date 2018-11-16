package de.noltarium.minecraft.archivator.bukkit.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import de.noltarium.minecraft.archivator.bukkit.chat.BackupChatNotificationService;
import de.noltarium.minecraft.archivator.bukkit.chat.ChatFacade;
import de.noltarium.minecraft.archivator.core.backup.BackupConfig;
import de.noltarium.minecraft.archivator.core.backup.BackupProcessFactory;
import de.noltarium.minecraft.archivator.core.backup.BackupProcessService;
import de.noltarium.minecraft.archivator.core.backup.ProcessListener;
import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;
import de.noltarium.minecraft.archivator.core.services.CopyFilesToFolderService;
import de.noltarium.minecraft.archivator.core.services.FilesCompressService;

public class PluginCommandConfigurationProxy {

	private final BackupDAO backupDAO;
	private final BackupConfig configFacade;
	private ChatFacade chat;

	public PluginCommandConfigurationProxy(BackupDAO backupDAO, ChatFacade chat, BackupConfig configFacade) {
		super();
		this.backupDAO = backupDAO;
		this.chat = chat;

		this.configFacade = configFacade;
	}

	public void configure(JavaPlugin plugin) throws InvalidConfigurationException {

		BackupChatNotificationService chatHelper = new BackupChatNotificationService(chat);

		List<ProcessListener<String>> startedListener = Arrays.asList(chatHelper.getChatStarted());
		List<ProcessListener<File>> finished = Arrays.asList(chatHelper.getChatSuccessFull());
		List<ProcessListener<Exception>> faildListeners = Arrays.asList(chatHelper.getChatFaild());

		plugin.getCommand("backup").setExecutor(new BackupPluginCommandExecutor(plugin,
				new BackupProcessService(
						new BackupProcessFactory(new FilesCompressService(), new CopyFilesToFolderService(),
								configFacade, backupDAO, startedListener, finished, faildListeners))));

		plugin.getCommand("backuplist").setExecutor(new BackupListCommandExecutor(backupDAO, chat));

	}

}
