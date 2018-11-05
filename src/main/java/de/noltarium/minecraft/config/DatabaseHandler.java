package de.noltarium.minecraft.config;

import javax.sql.DataSource;

import de.noltarium.minecraft.database.DataSourceFactory;
import lombok.Data;

@Data
public class DatabaseHandler {

	private final String tablePrefix;
	private final DataSource dataSource;

	public DatabaseHandler(String tablePrefix, DataSourceFactory datasourceFactory) {
		super();
		this.tablePrefix = tablePrefix;
		this.dataSource = datasourceFactory.createDataSource();
	}

}
