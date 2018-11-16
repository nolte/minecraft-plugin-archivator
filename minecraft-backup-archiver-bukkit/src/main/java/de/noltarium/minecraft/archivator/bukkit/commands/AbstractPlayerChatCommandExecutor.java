package de.noltarium.minecraft.archivator.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.noltarium.minecraft.archivator.bukkit.chat.ChatFacade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractPlayerChatCommandExecutor implements CommandExecutor {

	@Getter
	private final ChatFacade facade;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player)
			return onPlayerChatCommand((Player) sender, args);

		return false;
	}

	protected abstract boolean onPlayerChatCommand(Player player, String[] args);

}
