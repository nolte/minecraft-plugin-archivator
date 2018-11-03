package de.noltarium.minecraft.database;

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

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import de.noltarium.minecraft.config.ArchivatorConfigurationFacade;
import de.noltarium.minecraft.database.model.BackupEntity;
import lombok.extern.java.Log;

@Log
public class DatabaseFacadeTest {

	private DatabaseFacade facade;

	@Before
	public void initFacade() {
		JDBCDataSource dataSource = new JDBCDataSource();
		dataSource.setURL("jdbc:hsqldb:mem:mymemdb");
		dataSource.setUser("SA");
		dataSource.setPassword("");

		new DatabaseMigratorService(dataSource, "arc_").migrateDatabase();
		facade = new DatabaseFacade(dataSource, "arc_", log);
	}

	@Test
	public void updateLastSuccessfullRun() {
		OffsetDateTime ofInstant = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541191505),
				ArchivatorConfigurationFacade.defaultTimeZone);
		facade.updateLastSuccessFullRun(ofInstant);
		Optional<OffsetDateTime> result = facade.loadLastSuccessfullRun();
		assertTrue(result.isPresent());
		assertThat(result.get(), is(ofInstant));

		OffsetDateTime ofInstantSecond = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541100000),
				ArchivatorConfigurationFacade.defaultTimeZone);
		facade.updateLastSuccessFullRun(ofInstantSecond);
		Optional<OffsetDateTime> resultSecondRun = facade.loadLastSuccessfullRun();
		assertTrue(resultSecondRun.isPresent());
		assertThat(resultSecondRun.get(), is(ofInstantSecond));

	}

	@Test
	public void insertNewBackendRun() throws Exception {
		OffsetDateTime ofInstant = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1541191505),
				ArchivatorConfigurationFacade.defaultTimeZone);
		BackupEntity build = BackupEntity.builder().backupRunId("abc").startTime(null).build();
		Integer inserted = facade.insertNewBackupRun(build);
		assertThat(inserted, is(1));
		build.setStartTime(ofInstant);
		Integer updated = facade.updateBackupRun(build);
		assertThat(updated, is(1));

		List<BackupEntity> runs = facade.loadBackupRuns();
		assertThat(runs.size(), is(1));
	}
	@Test
	public void testName() throws Exception {
		//get a predefined instance
		CronDefinition cronDefinition =
		CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);

		//create a parser based on provided definition
		CronParser parser = new CronParser(cronDefinition);
		Cron quartzCron = parser.parse("*/45 * * * * ?");
		
		CronDefinition foba = quartzCron.getCronDefinition();
		System.out.println(foba);
	}
}
