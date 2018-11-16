package de.noltarium.minecraft.archivator.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInitiatorHelper {

	public static void initPlugin(JavaPlugin plugin) {

		// create plugin folder if not exists
		File pluginConfigFolder = plugin.getDataFolder();
		pluginConfigFolder.mkdirs();

		YamlConfiguration pluginConfigFileDefaultConfiguration = new YamlConfiguration();
		InputStream in = PluginInitiatorHelper.class.getClassLoader().getResourceAsStream("config-default.yml");

		InputStreamReader reader = new InputStreamReader(in);
		try {
			pluginConfigFileDefaultConfiguration.load(reader);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		plugin.getConfig().setDefaults(pluginConfigFileDefaultConfiguration);

	}

}
