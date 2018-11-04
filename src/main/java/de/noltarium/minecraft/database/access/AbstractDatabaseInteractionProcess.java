package de.noltarium.minecraft.database.access;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import lombok.Data;

@Data
public abstract class AbstractDatabaseInteractionProcess<T> {

	private final DataSource datasource;
	private final Logger logger;

	public AbstractDatabaseInteractionProcess(DataSource datasource, Logger logger) {
		this.datasource = datasource;
		this.logger = logger;
	}

	public T executeConnectionTask() {
		Connection connection = null;
		try {
			logger.fine("update last successful backup");
			connection = datasource.getConnection();

			return executeConnectionTask(connection);
		} catch (SQLException e) {
			logger.severe("faild to execute sql");
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.severe("Fatal faild close database stuff ...");
				throw new RuntimeException("Finaly Faild to close the DatabaseConnection", e);
			}
		}

	}

	protected abstract T executeConnectionTask(Connection connection);

}
