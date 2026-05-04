package it.casino.model;

/**
 * Slot machine con tre rulli.
 * Calcola la vincita in base ai simboli ottenuti dopo ogni giro.
 */
public class SlotMachine {

    private static final int NUM_REELS = 3;

    private final Reel[] reels;
    private Symbol[] lastResult;

    public SlotMachine() {
        reels = new Reel[NUM_REELS];
        for (int i = 0; i < NUM_REELS; i++) {
            reels[i] = new Reel();
        }
        lastResult = new Symbol[NUM_REELS];
    }

    /** Costruttore con rulli personalizzati (per i test). */
    SlotMachine(Reel[] reels) {
        if (reels == null || reels.length != NUM_REELS) {
            throw new IllegalArgumentException("Sono richiesti esattamente 3 rulli.");
        }
        this.reels = reels.clone();
        lastResult = new Symbol[NUM_REELS];
    }

    /**
     * Esegue un giro e calcola la vincita.
     *
     * @param bet la puntata del giocatore
     * @return le monete vinte (0 se nessuna corrispondenza)
     */
    public int spin(int bet) {
        if (bet <= 0) {
            throw new IllegalArgumentException("La puntata deve essere positiva.");
        }
        for (int i = 0; i < NUM_REELS; i++) {
            lastResult[i] = reels[i].spin();
        }
        return calculateWinnings(bet);
    }

    /**
     * Calcola la vincita in base all'ultimo risultato.
     *
     * <ul>
     *   <li>Tutti e tre i simboli uguali → puntata × moltiplicatore del simbolo × 3</li>
     *   <li>Due simboli uguali qualunque → puntata × moltiplicatore del simbolo</li>
     *   <li>Nessuna corrispondenza → 0</li>
     * </ul>
     */
    private int calculateWinnings(int bet) {
        Symbol s0 = lastResult[0];
        Symbol s1 = lastResult[1];
        Symbol s2 = lastResult[2];

        if (s0 == s1 && s1 == s2) {
            // Jackpot: tutti e tre uguali
            return bet * s0.getMultiplier() * 3;
        }
        if (s0 == s1) {
            return bet * s0.getMultiplier();
        }
        if (s1 == s2) {
            return bet * s1.getMultiplier();
        }
        if (s0 == s2) {
            return bet * s0.getMultiplier();
        }
        return 0;
    }

    /** Restituisce una copia dell'ultimo risultato. */
    public Symbol[] getLastResult() {
        return lastResult.clone();
    }

    /** Verifica se l'ultimo giro ha prodotto un jackpot (tutti e tre uguali). */
    public boolean isJackpot() {
        return lastResult[0] != null
            && lastResult[0] == lastResult[1]
            && lastResult[1] == lastResult[2];
    }
}
