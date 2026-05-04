package it.casino.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SlotMachineTest {

    /** Random deterministico che restituisce sempre 0 → CILIEGIA per ogni rullo. */
    private static class FixedRandom extends Random {
        private final int fixedValue;

        FixedRandom(int fixedValue) {
            this.fixedValue = fixedValue;
        }

        @Override
        public int nextInt(int bound) {
            return fixedValue;
        }
    }

    private SlotMachine buildMachineWithSymbol(Symbol symbol) {
        // Creiamo 3 rulli dove spin() restituisce sempre il simbolo fisso
        Reel[] reels = new Reel[3];
        for (int i = 0; i < 3; i++) {
            reels[i] = new Reel(new FixedRandom(0)) {
                @Override
                public Symbol spin() {
                    setCurrentSymbol(symbol);
                    return symbol;
                }
            };
        }
        return new SlotMachine(reels);
    }

    @Test
    void jackpotWhenAllSymbolsMatch() {
        SlotMachine machine = buildMachineWithSymbol(Symbol.CILIEGIA);
        int winnings = machine.spin(10);
        // Jackpot: bet × moltiplicatore × 3 = 10 × 2 × 3 = 60
        assertEquals(60, winnings);
        assertTrue(machine.isJackpot());
    }

    @Test
    void noWinWhenNoMatch() {
        // Tre simboli diversi: creiamo i rulli manualmente
        Reel r0 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(Symbol.CILIEGIA); return Symbol.CILIEGIA; } };
        Reel r1 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(Symbol.ARANCIA);  return Symbol.ARANCIA;  } };
        Reel r2 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(Symbol.LIMONE);   return Symbol.LIMONE;   } };

        SlotMachine machine = new SlotMachine(new Reel[]{r0, r1, r2});
        int winnings = machine.spin(10);
        assertEquals(0, winnings);
        assertFalse(machine.isJackpot());
    }

    @Test
    void twoMatchingSymbolsPayPartialWin() {
        // Rullo 0 e 1: SETTE; Rullo 2: CILIEGIA
        Symbol match = Symbol.SETTE;
        Reel r0 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(match); return match; } };
        Reel r1 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(match); return match; } };
        Reel r2 = new Reel() { @Override public Symbol spin() { setCurrentSymbol(Symbol.CILIEGIA); return Symbol.CILIEGIA; } };

        SlotMachine machine = new SlotMachine(new Reel[]{r0, r1, r2});
        int winnings = machine.spin(10);
        // Due simboli: bet × moltiplicatore = 10 × 50 = 500
        assertEquals(500, winnings);
        assertFalse(machine.isJackpot());
    }

    @Test
    void spinRejectsNonPositiveBet() {
        SlotMachine machine = buildMachineWithSymbol(Symbol.BAR);
        assertThrows(IllegalArgumentException.class, () -> machine.spin(0));
        assertThrows(IllegalArgumentException.class, () -> machine.spin(-5));
    }

    @Test
    void getLastResultReturnsCopy() {
        SlotMachine machine = buildMachineWithSymbol(Symbol.LUIGI);
        machine.spin(1);
        Symbol[] result = machine.getLastResult();
        result[0] = Symbol.CILIEGIA; // modifica la copia
        // L'originale non deve essere cambiato
        assertSame(Symbol.LUIGI, machine.getLastResult()[0]);
    }

    @Test
    void constructorRejectsWrongNumberOfReels() {
        assertThrows(IllegalArgumentException.class,
            () -> new SlotMachine(new Reel[]{new Reel()}));
    }
}
