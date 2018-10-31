package de.noltarium.minecraft.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class FileCompress {

	public File compressFiles(Collection<File> files, File file, ArchiveType archiveType) throws IOException {
		// Create the output stream for the output file
		File archiveFile = null;
		FileOutputStream fos = null;
		archiveFile = new File(file.getCanonicalPath());
		try {
			fos = new FileOutputStream(archiveFile);
			switch (archiveType) {
			case TAR:
				TarArchiveOutputStream taos = null;
				// Wrap the output file stream in streams that will tar and gzip everything
				try {
					taos = new TarArchiveOutputStream(new GZIPOutputStream(new BufferedOutputStream(fos)));
					// TAR has an 8 gig file limit by default, this gets around that
					taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR); // to get past the 8 gig limit
					// TAR originally didn't support long file names, so enable the support for it
					taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
					// Get to putting all the files in the compressed output file
					appendAllFilesToArchive(files, archiveType, taos);
					// Close everything up
				} finally {
					if (taos != null)
						taos.close();
				}

				fos.close();
				break;
			case ZIP:
				ZipArchiveOutputStream zaos = null;
				try {
					// Wrap the output file stream in streams that will tar and zip everything
					zaos = new ZipArchiveOutputStream(new BufferedOutputStream(fos));
					zaos.setEncoding("UTF-8");
					zaos.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.ALWAYS);

					// Get to putting all the files in the compressed output file
					appendAllFilesToArchive(files, archiveType, zaos);

					// Close everything up

					break;
				} finally {
					if (zaos != null)
						zaos.close();
				}
			}
			return archiveFile;
		} finally {
			if (fos != null)
				fos.close();
		}
	}

	private void appendAllFilesToArchive(Collection<File> files, ArchiveType archiveType, ArchiveOutputStream aos)
			throws IOException {
		for (File f : files) {
			addFilesToCompression(aos, f, ".", archiveType);
		}
	}

	private void addFilesToCompression(ArchiveOutputStream aos, File file, String dir, ArchiveType archiveType)
			throws IOException {

		// Create an entry for the file
		switch (archiveType) {
		case TAR:
			aos.putArchiveEntry(new TarArchiveEntry(file, dir + "/" + file.getName()));
			break;
		case ZIP:
			aos.putArchiveEntry(new ZipArchiveEntry(file, dir + "/" + file.getName()));
			break;
		}

		if (file.isFile()) {
			// Add the file to the archive
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			IOUtils.copy(bis, aos);
			aos.closeArchiveEntry();
			bis.close();

		} else if (file.isDirectory()) {
			// close the archive entry
			aos.closeArchiveEntry();
			// go through all the files in the directory and using recursion, add them to
			// the archive

			for (File childFile : file.listFiles()) {
				addFilesToCompression(aos, childFile, dir + "/" + file.getName(), archiveType);
			}
		}

	}

}