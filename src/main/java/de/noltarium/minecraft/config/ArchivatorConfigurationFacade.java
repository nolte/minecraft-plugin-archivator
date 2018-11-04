package de.noltarium.minecraft.config;

import static de.noltarium.minecraft.utils.FolderUtil.createFolderIfNotExists;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import de.noltarium.minecraft.Archivator;
import de.noltarium.minecraft.backup.BackupConfigProvider;
import de.noltarium.minecraft.database.DataSourceFactory;
import de.noltarium.minecraft.database.SQLiteDataSourceFactory;
import de.noltarium.minecraft.database.SupportedDatabaseType;

public class ArchivatorConfigurationFacade {

	public static final DateTimeFormatter humanReadableDateFormat = DateTimeFormatter.ofPattern("YYY-MM-dd HH:mm:ss Z");
	public static final String NEWLINE = System.lineSeparator();
	public static final ZoneId defaultTimeZone = ZoneOffset.UTC;

	protected static final String BACKUP_PROPERTY_PREFIX = "archivator.";

//	
	private static final String BACKUP_PROPERTY_DATABASE_PREFIX = BACKUP_PROPERTY_PREFIX + "database.";
	public static final String BACKUP_DATABASE_TYPE_KEY = BACKUP_PROPERTY_DATABASE_PREFIX + ".type";
	public static final String BACKUP_DATABASE_FILE_KEY = BACKUP_PROPERTY_DATABASE_PREFIX + ".file";
	public static final String BACKUP_DATABASE_TABLE_PREFIX_KEY = BACKUP_PROPERTY_DATABASE_PREFIX + ".table_prefix";

	private final Archivator archivator;

	public ArchivatorConfigurationFacade(Archivator archivator) throws InvalidConfigurationException {
		this.archivator = archivator;
		initConfig(archivator);
	}

	private void initConfig(Archivator archivator) throws InvalidConfigurationException {
		try {
			// check that the plugin folder is presented
			File pluginDataFolder = archivator.getDataFolder();

			// create the plugin forlder if not exists
			createFolderIfNotExists(pluginDataFolder);

			// prepare the plugin config file (default bukkit config file name is
			// 'config.yml')
			File configFile = new File(pluginDataFolder, "config.yml");
			final YamlConfiguration pluginConfigFileConfigration = new YamlConfiguration();

			YamlConfiguration pluginConfigFileDefaultConfiguration = new YamlConfiguration();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("config-default.yml");
			pluginConfigFileDefaultConfiguration.load(new InputStreamReader(in));

			if (!configFile.exists()) {
				// load the existing config
				configFile.createNewFile();
			} else {
				// merging the defaults with the existing config
				archivator.getLogger().info("merge configs");
				pluginConfigFileConfigration.load(configFile);
			}
			// check that all default config parameters are present in the file
			pluginConfigFileDefaultConfiguration.getKeys(true).forEach(defaultConfigKey -> {
				if (!pluginConfigFileConfigration.contains(defaultConfigKey)) {
					pluginConfigFileConfigration.set(defaultConfigKey,
							pluginConfigFileDefaultConfiguration.get(defaultConfigKey));
				}
			});

			pluginConfigFileConfigration.save(configFile);
			archivator.reloadConfig();
		} catch (IOException |

				InvalidConfigurationException e) {
			e.printStackTrace();
			throw new InvalidConfigurationException(e);
		}
	}

	public BackupConfigProvider getBackupConfigProvider() {
		return new BackupConfigFileProvider(archivator.getDataFolder(), archivator.getConfig());
	}

	public DatabaseHandler getDatabaseHandler() throws InvalidConfigurationException {

		SupportedDatabaseType type = SupportedDatabaseType
				.fromString(archivator.getConfig().getString(BACKUP_DATABASE_TYPE_KEY).toLowerCase());
		String tablePrefix = archivator.getConfig().getString(BACKUP_DATABASE_TABLE_PREFIX_KEY);
		DataSourceFactory dsFactory = null;
		switch (type) {
		case SQLITE:
			dsFactory = new SQLiteDataSourceFactory(
					archivator.getDataFolder() + "/" + archivator.getConfig().getString(BACKUP_DATABASE_FILE_KEY));
			break;
		default:
			throw new InvalidConfigurationException("Unsupported sql database type:" + type);
		}

		return new DatabaseHandler(tablePrefix, dsFactory);
	}

}
