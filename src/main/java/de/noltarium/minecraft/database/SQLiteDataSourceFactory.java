package de.noltarium.minecraft.database;

import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;

import lombok.Data;

@Data
public class SQLiteDataSourceFactory implements DataSourceFactory {

	private final String databaseFile;

	public SQLiteDataSourceFactory(String databaseFile) {
		this.databaseFile = databaseFile;
	}

	@Override
	public DataSource createDataSource() {
		SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:" + databaseFile);
		return dataSource;
	}

}
