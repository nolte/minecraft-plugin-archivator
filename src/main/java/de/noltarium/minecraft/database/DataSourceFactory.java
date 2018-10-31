package de.noltarium.minecraft.database;

import javax.sql.DataSource;

public interface DataSourceFactory {

	DataSource createDataSource();

}
