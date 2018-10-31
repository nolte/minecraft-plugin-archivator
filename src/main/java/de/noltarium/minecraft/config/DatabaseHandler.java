package de.noltarium.minecraft.config;

import javax.sql.DataSource;

import de.noltarium.minecraft.database.DataSourceFactory;
import de.noltarium.minecraft.database.SupportedDatabaseType;
import lombok.Data;

@Data
public class DatabaseHandler {

	private final SupportedDatabaseType type;
	private final String tablePrefix;
	private final DataSource dataSource;

	public DatabaseHandler(SupportedDatabaseType type, String tablePrefix, DataSourceFactory datasourceFactory) {
		super();
		this.type = type;
		this.tablePrefix = tablePrefix;
		this.dataSource = datasourceFactory.createDataSource();
	}

}
