package de.noltarium.minecraft.archivator.core.database.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import lombok.Data;

@Data
public abstract class AbstractStatementInteractionProcess<T> extends AbstractDatabaseInteractionProcess<T> {

	private final String query;

	public AbstractStatementInteractionProcess(String query, DataSource datasource, Logger logger) {
		super(datasource, logger);
		this.query = query;
	}

	@Override
	protected T executeConnectionTask(Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(query);
			configurePreparedStatement(statement);
			T workWithPreparedStatement = workWithPreparedStatement(statement);
			return workWithPreparedStatement;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

	}

	protected abstract T workWithPreparedStatement(PreparedStatement statement) throws SQLException;

	protected abstract void configurePreparedStatement(PreparedStatement statement) throws SQLException;;
}
