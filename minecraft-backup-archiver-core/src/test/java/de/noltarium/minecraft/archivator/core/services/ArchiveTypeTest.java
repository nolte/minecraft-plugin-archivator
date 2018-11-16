package de.noltarium.minecraft.archivator.core.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.noltarium.minecraft.archivator.core.services.FilesCompressService.ArchiveType;

public class ArchiveTypeTest {

	@Test
	public void valueOfEnum() throws Exception {
		assertThat(ArchiveType.TAR.value(), is("tar"));
	}

	@Test
	public void convertStringToEnum() throws Exception {
		assertThat(ArchiveType.fromString("tar"), is(ArchiveType.TAR));
	}

	@Test(expected = IllegalArgumentException.class)
	public void convertStringToEnumNotValidValue() throws Exception {
		ArchiveType.fromString("notExistsingArchiveType");
	}

}
