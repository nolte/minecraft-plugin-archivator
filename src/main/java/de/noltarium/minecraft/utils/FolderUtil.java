package de.noltarium.minecraft.utils;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FolderUtil {

	public static boolean createFolderIfNotExists(Path dataFolder) {
		return createFolderIfNotExists(dataFolder.toFile());
	}

	public static boolean createFolderIfNotExists(File dataFolder) {
		if (!dataFolder.exists()) {
			return dataFolder.mkdirs();
		}
		return false;
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
