package core.network;

import core.constants.Config;
import core.logic.GameLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkProtocolTest {

    @Test
    void resultRoundTripWithoutWinner() {
        NetworkMoveResult in = new NetworkMoveResult(3, 5, 1, 2, false, Config.EMPTY, null);
        NetworkMoveResult out = NetworkProtocol.parseResult(NetworkProtocol.encodeResult(in));
        assertEquals(3, out.getCol());
        assertEquals(5, out.getRow());
        assertEquals(1, out.getPlayer());
        assertEquals(2, out.getCurrentPlayer());
        assertFalse(out.isGameOver());
        assertEquals(Config.EMPTY, out.getWinner());
        assertNull(out.getWinningCells());
    }

    @Test
    void resultRoundTripWithWinningCells() {
        GameLogic g = new GameLogic();
        for (int c : new int[]{0, 0, 1, 1, 2, 2}) {
            g.dropDisc(c);
        }
        int row = g.dropDisc(3);
        NetworkMoveResult in = NetworkProtocol.buildFromLogic(3, row, Config.PLAYER_1, g);
        NetworkMoveResult out = NetworkProtocol.parseResult(NetworkProtocol.encodeResult(in));
        assertTrue(out.isGameOver());
        assertEquals(Config.PLAYER_1, out.getWinner());
        assertArrayEquals(g.getWinningCells(), out.getWinningCells());
    }

    @Test
    void namesAreSanitized() {
        assertEquals("JOIN|AliVeli", NetworkProtocol.encodeJoin(" Ali|Veli "));
        assertEquals("WELCOME|2|A|B", NetworkProtocol.encodeWelcome(2, "A|", "|B"));
    }

    @Test
    void malformedResultIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> NetworkProtocol.parseResult("RESULT|1|2"));
    }
}
