package no.nkopperudmoen.DAL;

import no.nkopperudmoen.UTIL.UUIDFetcher;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    PlayerRepository repo;
    PlayerController controller;
    UUID uuid = UUID.fromString("5e3106d6-8179-4a73-aa3e-21b5cc25dd59");
    @Mock
    Player player = PowerMockito.mock(Player.class);
    @Mock
    PlayerJoinEvent joinEvent = PowerMockito.mock(PlayerJoinEvent.class);

    @BeforeEach
    public void setUp() throws SQLException {
        TestDBConnection connection = new TestDBConnection();
        repo = new PlayerRepository(connection.getConnection());
        controller = new PlayerController(repo);
        PowerMockito.when(player.getUniqueId()).thenReturn(uuid);
        PowerMockito.when(player.getName()).thenReturn("itsNikolai11");
        PowerMockito.when(joinEvent.getPlayer()).thenReturn(player);
    }

    @Test
    void updateMapOnJoin() {
        controller.updateUUIDMap(player);
        UUID fromMap = repo.getUUID("itsNikolai11");
        assertNotNull(fromMap);
        assertEquals(uuid, fromMap);
    }

    @Test
    void updateMapCollidingNames_firstJoin() {
        UUID uuid1 = UUID.fromString("5e3106d6-8179-4a73-aa3e-21b5cc25dd59");
        UUID uuid2 = UUID.fromString("4b897729-ce3c-404c-b2f8-308eaef1dc83");

        Player player1 = PowerMockito.mock(Player.class);
        Player player2 = PowerMockito.mock(Player.class);

        PowerMockito.when(player1.getName()).thenReturn("itsNikolai11");
        PowerMockito.when(player1.getUniqueId()).thenReturn(uuid1);
        controller.updateUUIDMap(player1);
        assertEquals(uuid1, repo.getUUID("itsNikolai11"));

        PowerMockito.when(player2.getName()).thenReturn("itsNikolai11");
        PowerMockito.when(player2.getUniqueId()).thenReturn(uuid2);

        controller.updateUUIDMap(player2);
        assertEquals(uuid2, repo.getUUID("itsNikolai11"));
        assertEquals(repo.getName(uuid1), "EmptyName");
    }

    @Test
    void updateMapCollidingNames_BothExists() {
        UUID uuid1 = UUID.fromString("5e3106d6-8179-4a73-aa3e-21b5cc25dd59");
        UUID uuid2 = UUID.fromString("4b897729-ce3c-404c-b2f8-308eaef1dc83");

        Player player1 = PowerMockito.mock(Player.class);
        Player player2 = PowerMockito.mock(Player.class);

        PowerMockito.when(player1.getName()).thenReturn("itsNikolai11");
        PowerMockito.when(player1.getUniqueId()).thenReturn(uuid1);
        controller.updateUUIDMap(player1);
        assertEquals(uuid1, repo.getUUID("itsNikolai11"));

        PowerMockito.when(player2.getName()).thenReturn("TestName");
        PowerMockito.when(player2.getUniqueId()).thenReturn(uuid2);
        controller.updateUUIDMap(player2);
        PowerMockito.when(player2.getName()).thenReturn("itsNikolai11");
        controller.updateUUIDMap(player2);
        assertEquals(uuid2, repo.getUUID("itsNikolai11"));
        assertEquals(repo.getName(uuid1), "EmptyName");
    }

    @Test
    void updateOnQuit() {
        throw new NotImplementedException();
    }

    @Test
    void getFirstJoined() {
        throw new NotImplementedException();

    }

    @Test
    void getLastOnline() {
        throw new NotImplementedException();

    }

    @Test
    void getJoinedAsNumber() {
        throw new NotImplementedException();

    }
}