package de.noltarium.minecraft;

import static org.junit.Assert.assertTrue;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.Before;
import org.junit.Test;

import de.noltarium.minecraft.database.DatabaseFacade;
import de.noltarium.minecraft.database.DatabaseMigratorService;
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

		facade.updateLastSuccessFullRun(OffsetDateTime.now());

		Optional<OffsetDateTime> result = facade.loadLastSuccessfullRun();
		assertTrue(result.isPresent());

	}

}
