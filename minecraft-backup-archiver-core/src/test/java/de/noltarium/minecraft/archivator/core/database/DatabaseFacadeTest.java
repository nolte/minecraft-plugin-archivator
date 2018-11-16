package de.noltarium.minecraft.archivator.core.database;

import static de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade.defaultTimeZone;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;
import de.noltarium.minecraft.archivator.core.database.model.BackupEntity;
import lombok.extern.java.Log;

@Log
public class DatabaseFacadeTest {

	private JDBCDataSource dataSource;

	@Before
	public void initFacade() {
		dataSource = new JDBCDataSource();
		dataSource.setURL("jdbc:hsqldb:mem:mymemdb");
		dataSource.setUser("SA");
		dataSource.setPassword("");

		new DatabaseMigratorService(dataSource, "arc_").migrateDatabase();
		;
	}

	@Test
	public void updateLastSuccessfullRun() {
		OffsetDateTime ofInstant = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541191505), defaultTimeZone);
		DatabaseFacade facade = new DatabaseFacade(dataSource, "arc_", log);

		facade.updateLastSuccessFullRun(ofInstant);

		Optional<OffsetDateTime> result = facade.loadLastSuccessfullRun();
		assertTrue(result.isPresent());
		assertThat(result.get(), is(ofInstant));

		OffsetDateTime ofInstantSecond = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541100000), defaultTimeZone);
		facade.updateLastSuccessFullRun(ofInstantSecond);
		Optional<OffsetDateTime> resultSecondRun = facade.loadLastSuccessfullRun();
		assertTrue(resultSecondRun.isPresent());
		assertThat(resultSecondRun.get(), is(ofInstantSecond));

	}

	@Test
	public void insertNewBackendRun() throws Exception {
		OffsetDateTime ofInstant = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541191505), defaultTimeZone);
		BackupEntity build = BackupEntity.builder().backupRunId("abc").startTime(null).build();
		BackupDAO facade = new BackupDAO(dataSource, "arc_", log);
		Integer inserted = facade.insertNewBackupRun(build);
		assertThat(inserted, is(1));
		build.setStartTime(ofInstant);
		Integer updated = facade.updateBackupRun(build);
		assertThat(updated, is(1));

		List<BackupEntity> runs = facade.loadBackupRuns();
		assertThat(runs.size(), is(1));
	}
}
