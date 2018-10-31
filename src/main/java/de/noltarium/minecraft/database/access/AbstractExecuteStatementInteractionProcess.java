package de.noltarium.minecraft.database.access;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public abstract class AbstractExecuteStatementInteractionProcess extends AbstractStatementInteractionProcess<Boolean> {

	public AbstractExecuteStatementInteractionProcess(String query, DataSource datasource, Logger logger) {
		super(query, datasource, logger);
	}

	@Override
	protected Boolean workWithPreparedStatement(PreparedStatement statement) throws SQLException {
		return statement.execute();
	}

}
