package it.casino.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RouletteGameTest {

    /** Random che restituisce sempre lo stesso numero fisso. */
    private static class FixedRandom extends Random {
        private final int value;
        FixedRandom(int value) { this.value = value; }

        @Override
        public int nextInt(int bound) { return value; }
    }

    @Test
    void spinReturnsValueBetween0And36() {
        RouletteGame game = new RouletteGame();
        int result = game.spin();
        assertTrue(result >= 0 && result <= 36);
    }

    @Test
    void winOnCorrectNumber() {
        RouletteGame game = new RouletteGame(new FixedRandom(7));
        game.spin(); // → 7
        assertTrue(game.wins(RouletteGame.BetType.NUMERO, 7));
    }

    @Test
    void loseOnWrongNumber() {
        RouletteGame game = new RouletteGame(new FixedRandom(7));
        game.spin();
        assertFalse(game.wins(RouletteGame.BetType.NUMERO, 5));
    }

    @Test
    void winOnRosso() {
        // 1 è rosso
        RouletteGame game = new RouletteGame(new FixedRandom(1));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.ROSSO, 0));
    }

    @Test
    void winOnNero() {
        // 2 è nero
        RouletteGame game = new RouletteGame(new FixedRandom(2));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.NERO, 0));
    }

    @Test
    void winOnPari() {
        RouletteGame game = new RouletteGame(new FixedRandom(4));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.PARI, 0));
    }

    @Test
    void loseOnPariWhenZero() {
        RouletteGame game = new RouletteGame(new FixedRandom(0));
        game.spin();
        assertFalse(game.wins(RouletteGame.BetType.PARI, 0));
    }

    @Test
    void winOnDispari() {
        RouletteGame game = new RouletteGame(new FixedRandom(3));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.DISPARI, 0));
    }

    @Test
    void winOnBassa() {
        RouletteGame game = new RouletteGame(new FixedRandom(10));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.BASSA, 0));
    }

    @Test
    void winOnAlta() {
        RouletteGame game = new RouletteGame(new FixedRandom(20));
        game.spin();
        assertTrue(game.wins(RouletteGame.BetType.ALTA, 0));
    }

    @Test
    void calculateWinningsNumberPays36x() {
        RouletteGame game = new RouletteGame(new FixedRandom(7));
        game.spin();
        assertEquals(360, game.calculateWinnings(10, RouletteGame.BetType.NUMERO, 7));
    }

    @Test
    void calculateWinningsColorPays2x() {
        RouletteGame game = new RouletteGame(new FixedRandom(1)); // 1 è rosso
        game.spin();
        assertEquals(20, game.calculateWinnings(10, RouletteGame.BetType.ROSSO, 0));
    }

    @Test
    void calculateWinningsReturns0WhenLost() {
        RouletteGame game = new RouletteGame(new FixedRandom(2)); // 2 è nero
        game.spin();
        assertEquals(0, game.calculateWinnings(10, RouletteGame.BetType.ROSSO, 0));
    }

    @Test
    void getLastSpinResultThrowsBeforeSpin() {
        RouletteGame game = new RouletteGame();
        assertThrows(IllegalStateException.class, game::getLastSpinResult);
    }

    @Test
    void calculateWinningsThrowsBeforeSpin() {
        RouletteGame game = new RouletteGame();
        assertThrows(IllegalStateException.class,
            () -> game.calculateWinnings(10, RouletteGame.BetType.ROSSO, 0));
    }
}
