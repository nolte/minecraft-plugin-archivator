package de.noltarium.minecraft.archivator.bukkit.commands;

import static de.noltarium.minecraft.archivator.core.backup.AbstractBackupConfigFacade.NEWLINE;
import static de.noltarium.minecraft.archivator.core.model.SpecialChars.CHECK;
import static de.noltarium.minecraft.archivator.core.model.SpecialChars.CROSS;

import java.util.List;

import org.bukkit.entity.Player;

import de.noltarium.minecraft.archivator.bukkit.chat.ChatFacade;
import de.noltarium.minecraft.archivator.core.database.dao.BackupDAO;
import de.noltarium.minecraft.archivator.core.database.model.BackupEntity;;

public class BackupListCommandExecutor extends AbstractPlayerChatCommandExecutor {

	private final BackupDAO backupDAO;

	public BackupListCommandExecutor(BackupDAO backupDAO, ChatFacade facade) {
		super(facade);
		this.backupDAO = backupDAO;
	}

	@Override
	protected boolean onPlayerChatCommand(Player player, String[] args) {

		StringBuffer existingBackupsMessage = new StringBuffer();
		existingBackupsMessage.append("Existing Backups:").append(NEWLINE);
		List<BackupEntity> allRuns = backupDAO.loadBackupRuns();

		for (BackupEntity backupEntity : allRuns) {
			existingBackupsMessage.append("  - ").append(backupEntity.getBackupRunId());
			existingBackupsMessage.append(" ").append(backupEntity.getStartTime());
			if (backupEntity.getFsRemovedAt() == null)
				existingBackupsMessage.append(" ").append(CHECK).append(" ");
			else
				existingBackupsMessage.append(" ").append(CROSS).append(" ");

			existingBackupsMessage.append(NEWLINE);
		}

		getFacade().sendMessage(player, existingBackupsMessage.toString());

		return true;
	}

	String newString(int codePoint) {
		if (Character.charCount(codePoint) == 1) {
			return String.valueOf((char) codePoint);
		} else {
			return new String(Character.toChars(codePoint));
		}
	}

}
