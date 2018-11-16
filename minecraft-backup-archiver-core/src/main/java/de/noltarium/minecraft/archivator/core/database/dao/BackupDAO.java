package de.noltarium.minecraft.archivator.core.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import de.noltarium.minecraft.archivator.core.database.access.AbstractExecuteStatementInteractionProcess;
import de.noltarium.minecraft.archivator.core.database.access.AbstractReadStatementInteractionProcess;
import de.noltarium.minecraft.archivator.core.database.model.BackupEntity;

public class BackupDAO extends AbstractDAO {

	// TODO add methodes
	// * loadLastSuccessFullBackup(),
	// * makedBackupAsRemoved(date,user)

	public BackupDAO(DataSource datasource, String tablePrefix, Logger logger) {
		super(datasource, tablePrefix, logger);
	}

	public Integer insertNewBackupRun(BackupEntity run) {
		return (new AbstractExecuteStatementInteractionProcess(
				"INSERT INTO " + getTablePrefix()
						+ "BACKUPS (ID,startedAt,finishedAt,initiator,createdAt) VALUES( ?,?,?,?,?);",
				getDatasource(), getLogger()) {

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

	public Integer updateBackupRunAsRemoved(String backupId, OffsetDateTime removeDate, String removeInitialtor) {

		return (new AbstractExecuteStatementInteractionProcess(
				"UPDATE " + getTablePrefix() + "BACKUPS SET fsRemovedAt=?, fsRemovedBy=? WHERE ID = ?;",
				getDatasource(), getLogger()) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
				appendOffsetTime(statement, removeDate, 1);
				statement.setString(2, removeInitialtor);
				statement.setString(3, backupId);
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

	public Integer updateBackupRun(BackupEntity run) {
		return (new AbstractExecuteStatementInteractionProcess("UPDATE " + getTablePrefix()
				+ "BACKUPS SET startedAt=?, finishedAt=?, initiator=?, backupFile=?, backupFileSize=? WHERE ID = ?;",
				getDatasource(), getLogger()) {

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {

				int i = 1;
				appendOffsetTime(statement, run.getStartTime(), i++);
				appendOffsetTime(statement, run.getEndTime(), i++);
				statement.setString(i++, run.getTrigger());
				statement.setString(i++, run.getBackupFile());
				Long backupFileSize = run.getBackupFileSize();

				if (backupFileSize != null)
					statement.setLong(i++, backupFileSize);
				else
					statement.setNull(i++, Types.BIGINT);

				statement.setString(i++, run.getBackupRunId());
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
				"SELECT * FROM " + getTablePrefix() + "BACKUPS ORDER BY startedAt DESC;", getDatasource(),
				getLogger()) {
			@Override
			protected List<BackupEntity> workWithResultSet(ResultSet rs) throws SQLException {
				List<BackupEntity> backups = new ArrayList<>();
				while (rs.next()) {
					backups.add(BackupEntity.builder().backupRunId(rs.getString("ID"))
							.startTime(toOffsetDate(rs.getDate("startedAt")))
							.endTime(toOffsetDate(rs.getDate("finishedAt"))).backupFile(rs.getString("backupFile"))
							.backupFileSize(rs.getLong("backupFileSize"))
							.createdAt(toOffsetDate(rs.getDate("createdAt"))).trigger(rs.getString("initiator"))
							.fsRemovedAt(toOffsetDate(rs.getDate("fsRemovedAt")))
							.fsRemovedBy(rs.getString("fsRemovedBy")).build());
				}
				return backups;
			}

			@Override
			protected void configurePreparedStatement(PreparedStatement statement) throws SQLException {
			}
		}).executeConnectionTask();

	}

}
