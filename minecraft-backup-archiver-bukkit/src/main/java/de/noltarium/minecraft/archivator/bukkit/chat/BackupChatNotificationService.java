package de.noltarium.minecraft.archivator.bukkit.chat;

import java.io.File;

import de.noltarium.minecraft.archivator.core.backup.ProcessListener;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackupChatNotificationService {

	private final ChatFacade chat;

	private final ProcessListener<File> chatSuccessFull = new ChatSuccessFullNotifyer();
	private final ProcessListener<Exception> chatFaild = new ChatFaildNotifyer();
	private final ProcessListener<String> chatStarted = new ChatStartedNotifyer();

	class ChatSuccessFullNotifyer implements ProcessListener<File> {

		@Override
		public void receiveProcessState(File eventObject) {
			chat.msgAdmins("Backup Successfull Finished at: " + eventObject.getPath());
		}
	}

	class ChatFaildNotifyer implements ProcessListener<Exception> {

		@Override
		public void receiveProcessState(Exception eventObject) {
			chat.msgAdmins("Backup Faild because: " + eventObject.getMessage());
		}

	}

	class ChatStartedNotifyer implements ProcessListener<String> {

		@Override
		public void receiveProcessState(String eventObject) {
			chat.msgAdmins("Backup Started: " + eventObject);
		}

	}

}
