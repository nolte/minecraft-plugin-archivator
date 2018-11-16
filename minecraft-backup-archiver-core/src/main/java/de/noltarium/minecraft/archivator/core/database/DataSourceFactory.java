package de.noltarium.minecraft.archivator.core.database;

import javax.sql.DataSource;

public interface DataSourceFactory {

	DataSource createDataSource();

}
