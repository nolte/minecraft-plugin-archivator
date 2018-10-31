package de.noltarium.minecraft.database.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public abstract class AbstractReadStatementInteractionProcess<T> extends AbstractStatementInteractionProcess<T> {

	public AbstractReadStatementInteractionProcess(String query, DataSource datasource, Logger logger) {
		super(query, datasource, logger);
	}

	@Override
	protected T workWithPreparedStatement(PreparedStatement statement) throws SQLException {
		return readResultSet(statement);

	}

	private T readResultSet(PreparedStatement statement) {
		ResultSet rs = null;
		try {
			rs = statement.executeQuery();
			return workWithResultSet(rs);
		} catch (SQLException e) {
			getLogger().severe("Faild to work with resultset from: " + getQuery());
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}

		}
	}

	protected abstract T workWithResultSet(ResultSet rs) throws SQLException;

}