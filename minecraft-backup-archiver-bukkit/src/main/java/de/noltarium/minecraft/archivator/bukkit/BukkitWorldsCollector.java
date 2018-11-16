package de.noltarium.minecraft.archivator.bukkit;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import de.noltarium.minecraft.archivator.core.backup.BackupFilesCollector;

public class BukkitWorldsCollector implements BackupFilesCollector {

	@Override
	public List<File> loadFiles() {
		return Bukkit.getServer().getWorlds().parallelStream().map(w -> w.getWorldFolder())
				.collect(Collectors.toList());
	}

}
