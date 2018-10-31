package de.noltarium.minecraft.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.logging.Logger;

import javax.sql.DataSource;

import de.noltarium.minecraft.config.DatabaseHandler;
import de.noltarium.minecraft.database.access.AbstractExecuteStatementInteractionProcess;
import de.noltarium.minecraft.database.access.AbstractReadStatementInteractionProcess;

public class DatabaseFacade {
	public static final String BACKUP_LAST_SUCCESSFUL_RUN_KEY = "lastSuccessfullRun";

	private final DataSource datasource;

	// using the bukkit plugin logger ...
	private final Logger logger;

	private final String tablePrefix;

	public DatabaseFacade(DatabaseHandler databaseHandler, Logger logger) {
		super();
		this.datasource = databaseHandler.getDataSource();
		this.tablePrefix = databaseHandler.getTablePrefix();
		this.logger = logger;
	}

	public void updateLastSuccessFullRun(OffsetDateTime date) {

		Optional<OffsetDateTime> last_update = loadLastSuccessfullRun();

		if (!last_update.isPresent()) {
			// insert the last update
			logger.fine("first successfull run insert the value");
			insertLastSuccessfullUpdate(date);
		} else {
			// update
			logger.fine("update successfull run insert the value");
			updateLastSuccessfullUpdate(date);
		}

	}

	private boolean updateLastSuccessfullUpdate(OffsetDateTime date) {
		return (new AbstractExecuteStatementInteractionProcess(
				"UPDATE " + tablePrefix + "CONFIGURATION SET VALUE = ? WHERE ID = ?", datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, BACKUP_LAST_SUCCESSFUL_RUN_KEY);
				statement.setString(2, Long.toString(date.toEpochSecond()));
			}
		}).executeConnectionTask();

	}

	private boolean insertLastSuccessfullUpdate(OffsetDateTime newTime) {
		return (new AbstractExecuteStatementInteractionProcess(
				"INSERT INTO " + tablePrefix + "CONFIGURATION (ID,VALUE) VALUES( ?,?)", datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, BACKUP_LAST_SUCCESSFUL_RUN_KEY);
				statement.setString(2, Long.toString(newTime.toEpochSecond()));
			}
		}).executeConnectionTask();

	}

	public Optional<OffsetDateTime> loadLastSuccessfullRun() {

		return (new AbstractReadStatementInteractionProcess<Optional<OffsetDateTime>>(
				"SELECT * FROM " + tablePrefix + "CONFIGURATION where id = ?", datasource, logger) {
			@Override
			protected Optional<OffsetDateTime> workWithResultSet(ResultSet rs) throws SQLException {
				if (!rs.next()) {
					// No config parm found
					return Optional.empty();
				} else {
					String user = rs.getString("value");
					return Optional
							.of(OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(user)), ZoneId.of("UTC")));
				}
			}

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, BACKUP_LAST_SUCCESSFUL_RUN_KEY);

			}
		}).executeConnectionTask();
	}

}
