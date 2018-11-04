package de.noltarium.minecraft.database;

import static de.noltarium.minecraft.config.ArchivatorConfigurationFacade.defaultTimeZone;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.sql.DataSource;

import de.noltarium.minecraft.config.DatabaseHandler;
import de.noltarium.minecraft.database.access.AbstractExecuteStatementInteractionProcess;
import de.noltarium.minecraft.database.access.AbstractReadStatementInteractionProcess;
import de.noltarium.minecraft.database.model.BackupEntity;

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

	public DatabaseFacade(DatabaseHandler databaseHandler, Logger logger) {
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

	public Integer insertNewBackupRun(BackupEntity run) {
		return (new AbstractExecuteStatementInteractionProcess(
				"INSERT INTO " + tablePrefix
						+ "BACKUPS (ID,startedAt,finishedAt,Initiator,createdAt) VALUES( ?,?,?,?,?);",
				datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				statement.setString(1, run.getBackupRunId());
				appendOffsetTime(statement, run.getStartTime(), 2);
				appendOffsetTime(statement, run.getEndTime(), 3);
				statement.setString(4, run.getTrigger());
				appendOffsetTime(statement, OffsetDateTime.now(), 5);
			}

			private void appendOffsetTime(PreparedStatement statement, OffsetDateTime startTime, int parameterIndex)
					throws SQLException {
				if (startTime != null) {
					statement.setDate(parameterIndex, toSQLDate(startTime));
				} else {
					statement.setNull(parameterIndex, Types.DATE);
				}
			}
		}).executeConnectionTask();

	}

	public Integer updateBackupRun(BackupEntity run) {
		return (new AbstractExecuteStatementInteractionProcess(
				"UPDATE " + tablePrefix
						+ "BACKUPS SET startedAt=?, finishedAt=?, initiator=?, backupFile=? WHERE ID = ?;",
				datasource, logger) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {

				appendOffsetTime(statement, run.getStartTime(), 1);
				appendOffsetTime(statement, run.getEndTime(), 2);
				statement.setString(3, run.getTrigger());
				statement.setString(4, run.getBackupFile());
				statement.setString(5, run.getBackupRunId());
			}

			private void appendOffsetTime(PreparedStatement statement, OffsetDateTime startTime, int parameterIndex)
					throws SQLException {
				if (startTime != null) {
					statement.setDate(parameterIndex, toSQLDate(startTime));
				} else {
					statement.setDate(parameterIndex, null);
				}
			}
		}).executeConnectionTask();

	}

	public synchronized List<BackupEntity> loadBackupRuns() {

		return (new AbstractReadStatementInteractionProcess<List<BackupEntity>>(
				"SELECT * FROM " + tablePrefix + "BACKUPS;", datasource, logger) {
			@Override
			protected List<BackupEntity> workWithResultSet(ResultSet rs) throws SQLException {
				List<BackupEntity> backups = new ArrayList<>();
				while (rs.next()) {
					backups.add(BackupEntity.builder().backupRunId(rs.getString("ID"))
							.startTime(toOffsetDate(rs.getDate("startedAt")))
							.endTime(toOffsetDate(rs.getDate("finishedAt"))).backupFile(rs.getString("backupFile"))
							.createdAt(toOffsetDate(rs.getDate("createdAt"))).trigger(rs.getString("initiator"))
							.build());
				}
				return backups;
			}

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
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
