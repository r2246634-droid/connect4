package core.logic;

import core.constants.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Connect4AITest {

    private static GameLogic play(int... cols) {
        GameLogic g = new GameLogic();
        for (int c : cols) {
            g.dropDisc(c);
        }
        return g;
    }

    @Test
    void alwaysReturnsPlayableColumn() {
        GameLogic g = play(0, 0, 0, 0, 0, 0); // Sütun 0 dolu
        for (int level = Connect4AI.EASY; level <= Connect4AI.EXPERT; level++) {
            int col = Connect4AI.chooseMove(g, level);
            assertTrue(g.isColumnPlayable(col), "seviye " + level + " dolu sütun seçti");
        }
    }

    @Test
    void returnsMinusOneWhenBoardIsFull() {
        // Kazanansız dolan tahta (GameLogicTest.fullBoardWithoutLineIsDraw ile aynı dizilim)
        GameLogic g = play(0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0,
                           2, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3, 2,
                           4, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5, 4,
                           6, 6, 6, 6, 6, 6);
        assertTrue(g.isDraw());
        assertEquals(-1, Connect4AI.chooseMove(g, Connect4AI.HARD));
    }

    @Test
    void takesImmediateWin() {
        // P2 (AI) sütun 6'da 3 taşa sahip; sıra P2'de → 6'yı oynayıp kazanmalı.
        GameLogic g = play(0, 6, 1, 6, 3, 6, 0);
        assertEquals(Config.PLAYER_2, g.getCurrentPlayer());
        for (int level = Connect4AI.MEDIUM; level <= Connect4AI.EXPERT; level++) {
            assertEquals(6, Connect4AI.chooseMove(g, level), "seviye " + level);
        }
    }

    @Test
    void blocksOpponentWin() {
        // P1 alt satırda 0,1,2'ye sahip; sıra P2'de → 3'ü kapatmalı.
        GameLogic g = play(0, 6, 1, 6, 2);
        assertEquals(Config.PLAYER_2, g.getCurrentPlayer());
        for (int level = Connect4AI.MEDIUM; level <= Connect4AI.EXPERT; level++) {
            assertEquals(3, Connect4AI.chooseMove(g, level), "seviye " + level);
        }
    }

    @Test
    void doesNotModifyRealBoard() {
        GameLogic g = play(3, 3);
        Connect4AI.chooseMove(g, Connect4AI.EXPERT);
        assertEquals(2, g.getMoveCount());
        assertEquals(Config.PLAYER_1, g.getCurrentPlayer());
    }
}
