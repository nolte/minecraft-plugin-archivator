package de.noltarium.minecraft.chat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

public class ChatFacadeTest {

	private static final String TEST_PLUGIN_PREFIX = "testPluginPrefix";
	private ChatFacade facade;

	// Bar bar = mock(Bar.class);
	@Before
	public void setUp() {
		facade = new ChatFacade(TEST_PLUGIN_PREFIX);
	}

	@Test
	public void sendMessageToOnePlayer() throws Exception {
		Player player = mock(Player.class);
		facade.sendMessage(player, "fofa");
		verify(player, times(1)).sendMessage("§7[§6" + TEST_PLUGIN_PREFIX + "§7] §ffofa");

	}

	@Test
	public void sendMessageToPlayers() throws Exception {
		Player player = mock(Player.class);
		Player playerTwo = mock(Player.class);
		facade.sendMessage(Arrays.asList(player, playerTwo), "fofa");
		verify(player, times(1)).sendMessage("§7[§6" + TEST_PLUGIN_PREFIX + "§7] §ffofa");
		verify(playerTwo, times(1)).sendMessage("§7[§6" + TEST_PLUGIN_PREFIX + "§7] §ffofa");
	}

}
