package de.noltarium.minecraft.archivator.core.database.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public abstract class AbstractExecuteStatementInteractionProcess extends AbstractStatementInteractionProcess<Integer> {

	public AbstractExecuteStatementInteractionProcess(String query, DataSource datasource, Logger logger) {
		super(query, datasource, logger);
	}

	@Override
	protected Integer workWithPreparedStatement(PreparedStatement statement) throws SQLException {
		return statement.executeUpdate();
	}

}
