package de.noltarium.minecraft.database;

import java.util.HashMap;
import java.util.Map;

import org.flywaydb.core.Flyway;

import de.noltarium.minecraft.config.DatabaseHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseMigratorService {

	private final DatabaseHandler handler;

	public void migrateDatabase() {
		Map<String, String> placeholders = new HashMap<>();
		String databasePluginTablePrefix = handler.getTablePrefix();
		placeholders.put("achivator_db_table_prefix", databasePluginTablePrefix);
		Flyway flyway = new Flyway();
		flyway.setClassLoader(getClass().getClassLoader());
		flyway.setDataSource(handler.getDataSource());
		flyway.setTable(databasePluginTablePrefix + "flyway_schema_history");
		flyway.setPlaceholders(placeholders);
		flyway.setValidateOnMigrate(false);

		// Start the migration
		flyway.migrate();
	}
}
