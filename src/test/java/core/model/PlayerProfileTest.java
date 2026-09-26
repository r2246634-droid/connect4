package core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerProfileTest {

    @Test
    void levelThresholdsGrowBy80() {
        PlayerProfile p = new PlayerProfile();
        assertEquals(1, p.getLevel());
        p.addXp(199);
        assertEquals(1, p.getLevel());
        p.addXp(1);           // 200 → seviye 2
        assertEquals(2, p.getLevel());
        assertEquals(280, p.xpToNextLevel()); // Sonraki eşik 200 + 280 = 480
        p.addXp(280);         // 480 → seviye 3
        assertEquals(3, p.getLevel());
        assertEquals(0, p.levelProgressPercent());
    }

    @Test
    void winStreakTracking() {
        PlayerProfile p = new PlayerProfile();
        p.recordWin();
        p.recordWin();
        p.recordLoss();
        p.recordWin();
        assertEquals(3, p.getWins());
        assertEquals(1, p.getLosses());
        assertEquals(1, p.getWinStreak());
        assertEquals(2, p.getBestWinStreak());
    }

    @Test
    void achievementsUnlockOnceAndGrantXp() {
        PlayerProfile p = new PlayerProfile();
        p.recordWin();
        List<String> first = p.newlyUnlocked(RewardManager.getAllAchievements());
        assertEquals(List.of("İlk Zafer"), first);
        assertEquals(50, p.getXp());
        assertTrue(p.newlyUnlocked(RewardManager.getAllAchievements()).isEmpty());
    }

    @Test
    void historyIsCappedAndSanitized() {
        PlayerProfile p = new PlayerProfile();
        for (int i = 0; i < 35; i++) {
            p.addHistory("maç " + i);
        }
        assertEquals(30, p.getGameHistory().size());
        assertTrue(p.getGameHistory().get(0).endsWith("maç 34")); // En yeni başta

        p.addHistory("Ali|Veli kazandı");
        String latest = p.getGameHistory().get(0);
        assertFalse(latest.contains("|"), "ayırıcı karakter kayda sızmamalı");
    }
}
