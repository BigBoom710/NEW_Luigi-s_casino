package it.casino.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void constructorSetsNameAndCoins() {
        Player p = new Player("Luigi", 100);
        assertEquals("Luigi", p.getName());
        assertEquals(100, p.getCoins());
    }

    @Test
    void constructorRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Player("", 50));
        assertThrows(IllegalArgumentException.class, () -> new Player("  ", 50));
    }

    @Test
    void constructorRejectsNegativeCoins() {
        assertThrows(IllegalArgumentException.class, () -> new Player("Mario", -1));
    }

    @Test
    void addCoinsIncreasesBalance() {
        Player p = new Player("Luigi", 50);
        p.addCoins(30);
        assertEquals(80, p.getCoins());
    }

    @Test
    void deductCoinsDecreasesBalance() {
        Player p = new Player("Luigi", 100);
        p.deductCoins(40);
        assertEquals(60, p.getCoins());
    }

    @Test
    void deductCoinsThrowsWhenInsufficientFunds() {
        Player p = new Player("Luigi", 10);
        assertThrows(IllegalStateException.class, () -> p.deductCoins(20));
    }

    @Test
    void canBetReturnsTrueWhenSufficient() {
        Player p = new Player("Luigi", 100);
        assertTrue(p.canBet(50));
        assertTrue(p.canBet(100));
    }

    @Test
    void canBetReturnsFalseWhenInsufficient() {
        Player p = new Player("Luigi", 10);
        assertFalse(p.canBet(11));
        assertFalse(p.canBet(0));
    }
}
