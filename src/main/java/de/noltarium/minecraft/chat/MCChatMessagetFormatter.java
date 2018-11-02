package de.noltarium.minecraft.chat;

import java.text.MessageFormat;
import java.util.Arrays;

import org.bukkit.ChatColor;

public class MCChatMessagetFormatter {

	private static final ChatColor defaultColor = ChatColor.GRAY;
	private static final ChatColor defaultTextColor = ChatColor.WHITE;
	private static final ChatColor pluginColor = ChatColor.GOLD;
	private static final MessageFormat ingameMessageFromat = new MessageFormat(
			defaultColor + "[" + pluginColor + "{0}" + defaultColor + "] " + defaultTextColor + "{1}");

	protected static String mediateMessage(String message, String plugin) {
		return ingameMessageFromat.format(Arrays.asList(plugin, message).toArray());
	}

}
