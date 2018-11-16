package de.noltarium.minecraft.archivator.bukkit.chat;

import static de.noltarium.minecraft.archivator.bukkit.chat.MCChatMessagetFormatter.mediateMessage;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatFacade implements ChatNotification {

	private final String messagePrefix;

	public ChatFacade(String messagePrefix) {
		this.messagePrefix = messagePrefix;
	}

	public void sendMessage(Player player, String message) {
		sendMessage(Arrays.asList(player), message);
	}

	public void sendMessage(List<Player> players, String message) {
		players.parallelStream().forEach(p -> {
			sendMessageToPlayer(p, message);
		});
	}

	private void sendMessageToPlayer(Player p, String message) {
		p.sendMessage(mediateMessage(message, messagePrefix));
	}

	@Override
	public synchronized void msgAdmins(String msg) {
		Bukkit.getWorlds().forEach(w -> {
			w.getPlayers().forEach(p -> {
				if (p.hasPermission("Archivator.backup")) {
					sendMessageToPlayer(p, msg);
				}
			});
		});
	}
}
