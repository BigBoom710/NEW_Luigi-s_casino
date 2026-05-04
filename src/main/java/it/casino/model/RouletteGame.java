package it.casino.model;

import java.util.Random;

/**
 * Logica di gioco della Roulette europea (37 numeri: 0-36).
 *
 * <p>Tipi di puntata supportati:
 * <ul>
 *   <li>Numero pieno (0-36) → paga 35×</li>
 *   <li>Rosso/Nero → paga 2×</li>
 *   <li>Pari/Dispari → paga 2×</li>
 *   <li>Bassa (1-18) / Alta (19-36) → paga 2×</li>
 * </ul>
 */
public class RouletteGame {

    /** Numeri rossi nella roulette europea standard. */
    private static final int[] RED_NUMBERS = {
        1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    };

    public enum BetType { NUMERO, ROSSO, NERO, PARI, DISPARI, BASSA, ALTA }

    private final Random random;
    private int lastSpinResult;
    private boolean spun;

    public RouletteGame() {
        this(new Random());
    }

    /** Costruttore con Random personalizzato (per i test). */
    public RouletteGame(Random random) {
        this.random = random;
        this.lastSpinResult = -1;
        this.spun = false;
    }

    /** Fa girare la ruota e restituisce il numero (0-36). */
    public int spin() {
        lastSpinResult = random.nextInt(37); // 0-36
        spun = true;
        return lastSpinResult;
    }

    /**
     * Calcola la vincita.
     *
     * @param bet      puntata
     * @param betType  tipo di puntata
     * @param number   numero scelto (rilevante solo per {@link BetType#NUMERO})
     * @return monete vinte (inclusa la restituzione della puntata); 0 se perso
     * @throws IllegalStateException se la ruota non è stata ancora girata
     */
    public int calculateWinnings(int bet, BetType betType, int number) {
        if (!spun) {
            throw new IllegalStateException("La ruota non è ancora stata girata.");
        }
        if (wins(betType, number)) {
            int multiplier = (betType == BetType.NUMERO) ? 36 : 2;
            return bet * multiplier;
        }
        return 0;
    }

    /** Verifica se la puntata ha vinto. */
    public boolean wins(BetType betType, int number) {
        return switch (betType) {
            case NUMERO  -> lastSpinResult == number;
            case ROSSO   -> isRed(lastSpinResult);
            case NERO    -> lastSpinResult != 0 && !isRed(lastSpinResult);
            case PARI    -> lastSpinResult != 0 && lastSpinResult % 2 == 0;
            case DISPARI -> lastSpinResult % 2 != 0;
            case BASSA   -> lastSpinResult >= 1 && lastSpinResult <= 18;
            case ALTA    -> lastSpinResult >= 19 && lastSpinResult <= 36;
        };
    }

    private boolean isRed(int n) {
        for (int red : RED_NUMBERS) {
            if (red == n) return true;
        }
        return false;
    }

    public int getLastSpinResult() {
        if (!spun) {
            throw new IllegalStateException("La ruota non è ancora stata girata.");
        }
        return lastSpinResult;
    }

    public boolean hasSpun() {
        return spun;
    }
}
