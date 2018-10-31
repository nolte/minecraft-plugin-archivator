package de.noltarium.minecraft.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;

import de.noltarium.minecraft.config.DatabaseHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseMigratorService {

	private final DataSource datasource;
	private final String prefix;

	public DatabaseMigratorService(DatabaseHandler handler) {
		this.datasource = handler.getDataSource();
		this.prefix = handler.getTablePrefix();
	}

	public void migrateDatabase() {
		Map<String, String> placeholders = new HashMap<>();
		placeholders.put("achivator_db_table_prefix", prefix);
		Flyway flyway = new Flyway();
		flyway.setClassLoader(getClass().getClassLoader());
		flyway.setDataSource(datasource);
		flyway.setTable(prefix + "flyway_schema_history");
		flyway.setPlaceholders(placeholders);
		flyway.setValidateOnMigrate(false);

		// Start the migration
		flyway.migrate();
	}

}
