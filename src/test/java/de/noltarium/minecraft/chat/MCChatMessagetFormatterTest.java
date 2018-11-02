package de.noltarium.minecraft.chat;

import static org.junit.Assert.*;

import org.junit.Test;

public class MCChatMessagetFormatterTest {

	@Test
	public void createSimpleFormattedChatMessage() throws Exception {
		String message = MCChatMessagetFormatter.mediateMessage("Some Test", "Archivator");
		assertEquals("§7[§6Archivator§7] §fSome Test", message);
	}
}
