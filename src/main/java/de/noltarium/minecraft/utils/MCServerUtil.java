package de.noltarium.minecraft.utils;

import org.bukkit.Bukkit;

public class MCServerUtil {

	public static void msgAdmins(String msg) {
		Bukkit.getWorlds().forEach(w -> {
			w.getPlayers().forEach(p -> {
				if (p.hasPermission("Archivator.backup")) {
					p.sendMessage(msg);
				}
			});
		});
	}

}
