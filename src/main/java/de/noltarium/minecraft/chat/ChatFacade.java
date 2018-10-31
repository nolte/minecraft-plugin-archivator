package de.noltarium.minecraft.chat;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatFacade implements ChatNotification {

	private final JavaPlugin plugin;

	private static final ChatColor defaultColor = ChatColor.GRAY;
	private static final ChatColor defaultTextColor = ChatColor.WHITE;
	private static final ChatColor pluginColor = ChatColor.GOLD;
	private static final MessageFormat ingameMessageFromat = new MessageFormat(
			defaultColor + "[" + pluginColor + "{0}" + defaultColor + "] " + defaultTextColor + "{1}");

	public ChatFacade(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void sendMessage(Player player, String message) {
		sendMessage(Arrays.asList(player), message);
	}

	public void sendMessage(List<Player> players, String message) {
		players.parallelStream().forEach(p -> {
			p.sendMessage(mediateMessage(message));
		});
	}

	private String mediateMessage(String message) {
		return ingameMessageFromat.format(Arrays.asList(plugin.getName(), message).toArray());
	}

	@Override
	public void msgAdmins(String msg) {
		Bukkit.getWorlds().forEach(w -> {
			w.getPlayers().forEach(p -> {
				if (p.hasPermission("Archivator.backup")) {
					p.sendMessage(mediateMessage(msg));
				}
			});
		});
	}
}
