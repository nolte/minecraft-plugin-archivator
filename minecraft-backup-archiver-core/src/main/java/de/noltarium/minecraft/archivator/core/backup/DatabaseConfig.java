package de.noltarium.minecraft.archivator.core.backup;

import javax.sql.DataSource;

public interface DatabaseConfig {

	public DataSource getDataSource();

	public String getTablePrefix();
}
