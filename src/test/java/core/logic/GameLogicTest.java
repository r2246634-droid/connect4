package core.logic;

import core.constants.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    /** Sütunları sırayla oynar (oyuncular otomatik olarak sırayla değişir). */
    private static GameLogic play(int... cols) {
        GameLogic g = new GameLogic();
        for (int c : cols) {
            assertTrue(g.dropDisc(c) >= 0, "hamle geçersiz: " + c);
        }
        return g;
    }

    @Test
    void discFallsToLowestEmptyRow() {
        GameLogic g = new GameLogic();
        assertEquals(Config.ROWS - 1, g.dropDisc(3));
        assertEquals(Config.ROWS - 2, g.dropDisc(3));
        assertEquals(Config.PLAYER_1, g.getCell(Config.ROWS - 1, 3));
        assertEquals(Config.PLAYER_2, g.getCell(Config.ROWS - 2, 3));
    }

    @Test
    void turnsAlternate() {
        GameLogic g = new GameLogic();
        assertEquals(Config.PLAYER_1, g.getCurrentPlayer());
        g.dropDisc(0);
        assertEquals(Config.PLAYER_2, g.getCurrentPlayer());
        g.dropDisc(0);
        assertEquals(Config.PLAYER_1, g.getCurrentPlayer());
    }

    @Test
    void fullColumnAndOutOfRangeAreRejected() {
        GameLogic g = play(0, 0, 0, 0, 0, 0);
        assertFalse(g.isColumnPlayable(0));
        assertEquals(-1, g.dropDisc(0));
        assertEquals(-1, g.dropDisc(-1));
        assertEquals(-1, g.dropDisc(Config.COLS));
        assertFalse(g.getPlayableColumns().contains(0));
    }

    @Test
    void horizontalWin() {
        // P1: 0,1,2,3 alt satır — P2: üstlerine
        GameLogic g = play(0, 0, 1, 1, 2, 2, 3);
        assertTrue(g.isGameOver());
        assertEquals(Config.PLAYER_1, g.getWinner());
        assertFalse(g.isDraw());
        assertEquals(4, g.getWinningCells().length);
    }

    @Test
    void verticalWin() {
        GameLogic g = play(0, 1, 0, 1, 0, 1, 0);
        assertTrue(g.isGameOver());
        assertEquals(Config.PLAYER_1, g.getWinner());
    }

    @Test
    void diagonalWin() {
        // P1 "/" çaprazı: (5,0) (4,1) (3,2) (2,3)
        GameLogic g = play(0, 1, 1, 2, 2, 3, 2, 3, 3, 6, 3);
        assertTrue(g.isGameOver());
        assertEquals(Config.PLAYER_1, g.getWinner());
    }

    @Test
    void winningCellsAreTheConnectedLine() {
        GameLogic g = play(0, 0, 1, 1, 3, 3, 2); // Son taş ortadan tamamlar
        int[][] cells = g.getWinningCells();
        assertNotNull(cells);
        for (int[] cell : cells) {
            assertEquals(Config.ROWS - 1, cell[0]);
            assertEquals(Config.PLAYER_1, g.getCell(cell[0], cell[1]));
        }
    }

    @Test
    void noMovesAfterGameOver() {
        GameLogic g = play(0, 1, 0, 1, 0, 1, 0);
        assertEquals(-1, g.dropDisc(4));
        assertEquals(7, g.getMoveCount());
    }

    @Test
    void fullBoardWithoutLineIsDraw() {
        // Sütun çiftlerini 3'er taşlık bloklarla doldurarak 4'lü oluşmayan bir tahta kurar.
        int[] order = {0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0,
                       2, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3, 2,
                       4, 5, 4, 5, 4, 5, 5, 4, 5, 4, 5, 4,
                       6, 6, 6, 6, 6, 6};
        GameLogic g = play(order);
        assertTrue(g.isGameOver());
        assertTrue(g.isDraw());
        assertEquals(Config.EMPTY, g.getWinner());
        assertEquals(Config.ROWS * Config.COLS, g.getMoveCount());
    }

    @Test
    void copyIsIndependent() {
        GameLogic original = play(3);
        GameLogic copy = new GameLogic(original);
        copy.dropDisc(4);
        assertEquals(Config.EMPTY, original.getCell(Config.ROWS - 1, 4));
        assertEquals(1, original.getMoveCount());
        assertEquals(2, copy.getMoveCount());
    }

    @Test
    void resetClearsEverything() {
        GameLogic g = play(0, 1, 0, 1, 0, 1, 0);
        g.reset();
        assertFalse(g.isGameOver());
        assertNull(g.getWinningCells());
        assertEquals(0, g.getMoveCount());
        assertEquals(Config.PLAYER_1, g.getCurrentPlayer());
        assertEquals(Config.COLS, g.getPlayableColumns().size());
    }
}
