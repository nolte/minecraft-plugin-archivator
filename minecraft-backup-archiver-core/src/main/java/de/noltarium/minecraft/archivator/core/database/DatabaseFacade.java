package de.noltarium.minecraft.archivator.core.database;

import static de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade.defaultTimeZone;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.logging.Logger;

import javax.sql.DataSource;

import de.noltarium.minecraft.archivator.core.backup.DatabaseConfig;
import de.noltarium.minecraft.archivator.core.database.access.AbstractExecuteStatementInteractionProcess;
import de.noltarium.minecraft.archivator.core.database.access.AbstractReadStatementInteractionProcess;

public class DatabaseFacade {
	public static final String BACKUP_LAST_SUCCESSFUL_RUN_KEY = "lastSuccessfullRun";

	private final DataSource datasource;

	// using the bukkit plugin logger ...
	private final Logger logger;

	private final String tablePrefix;

	public DatabaseFacade(DataSource datasource, String tablePrefix, Logger logger) {
		super();
		this.datasource = datasource;
		this.tablePrefix = tablePrefix;
		this.logger = logger;
	}

	public DatabaseFacade(DatabaseConfig databaseHandler, Logger logger) {
		super();
		this.datasource = databaseHandler.getDataSource();
		this.tablePrefix = databaseHandler.getTablePrefix();
		this.logger = logger;
	}

	public synchronized void updateLastSuccessFullRun(OffsetDateTime date) {

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

	private Integer updateLastSuccessfullUpdate(OffsetDateTime date) {
		return (new AbstractExecuteStatementInteractionProcess(
				"UPDATE " + tablePrefix + "CONFIGURATION SET VALUE = ? WHERE ID = ?;", datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(2, BACKUP_LAST_SUCCESSFUL_RUN_KEY);
				String string = Long.toString(date.toEpochSecond());
				statement.setString(1, string);

			}
		}).executeConnectionTask();

	}

	private Integer insertLastSuccessfullUpdate(OffsetDateTime newTime) {
		return (new AbstractExecuteStatementInteractionProcess(
				"INSERT INTO " + tablePrefix + "CONFIGURATION (ID,VALUE) VALUES( ?,?);", datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, BACKUP_LAST_SUCCESSFUL_RUN_KEY);
				String string = Long.toString(newTime.toEpochSecond());
				statement.setString(2, string);
			}
		}).executeConnectionTask();

	}

	public synchronized Optional<OffsetDateTime> loadLastSuccessfullRun() {

		return (new AbstractReadStatementInteractionProcess<Optional<OffsetDateTime>>(
				"SELECT * FROM " + tablePrefix + "CONFIGURATION where id = ?;", datasource, logger) {
			@Override
			protected Optional<OffsetDateTime> workWithResultSet(ResultSet rs) throws SQLException {
				if (!rs.next()) {
					// No config parm found
					return Optional.empty();
				} else {
					String user = rs.getString("value");
					return Optional
							.of(OffsetDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(user)), defaultTimeZone));
				}
			}

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, BACKUP_LAST_SUCCESSFUL_RUN_KEY);

			}
		}).executeConnectionTask();
	}

	private OffsetDateTime toOffsetDate(Date date) {
		if (date == null)
			return null;
		else
			return OffsetDateTime.ofInstant(Instant.ofEpochSecond(date.getTime()), defaultTimeZone);
	}

	private Date toSQLDate(OffsetDateTime date) {
		return new Date(date.toEpochSecond());
	}
}
