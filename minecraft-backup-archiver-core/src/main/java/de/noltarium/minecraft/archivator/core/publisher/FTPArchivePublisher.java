package de.noltarium.minecraft.archivator.core.publisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FTPArchivePublisher {

	String host;
	private int port;
	private String password;
	private String username;

	public boolean publishArchive(File archive) throws IOException {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		boolean resultOk = true;

		try {
			ftpClient.connect(host, port);
			resultOk &= ftpClient.login(username, password);
			fis = new FileInputStream(archive);
			String remoteResultFile = "";
			resultOk &= ftpClient.storeFile(remoteResultFile, fis);
			resultOk &= ftpClient.logout();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				/* nothing to do */}
			ftpClient.disconnect();
		}

		return resultOk;

	}

}
