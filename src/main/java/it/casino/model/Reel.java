package it.casino.model;

import java.util.Random;

/**
 * Un singolo rullo della slot machine.
 * Contiene una sequenza di simboli e può essere fatto girare.
 */
public class Reel {

    private static final Symbol[] REEL_SYMBOLS = {
        Symbol.CILIEGIA, Symbol.CILIEGIA, Symbol.CILIEGIA,
        Symbol.ARANCIA,  Symbol.ARANCIA,  Symbol.ARANCIA,
        Symbol.LIMONE,   Symbol.LIMONE,   Symbol.LIMONE,
        Symbol.STELLA,   Symbol.STELLA,
        Symbol.BAR,      Symbol.BAR,
        Symbol.LUIGI,
        Symbol.SETTE
    };

    private final Random random;
    private Symbol currentSymbol;

    public Reel() {
        this(new Random());
    }

    /** Costruttore che accetta un {@link Random} per testabilità. */
    public Reel(Random random) {
        this.random = random;
        this.currentSymbol = REEL_SYMBOLS[0];
    }

    /** Fa girare il rullo e restituisce il simbolo risultante. */
    public Symbol spin() {
        int index = random.nextInt(REEL_SYMBOLS.length);
        currentSymbol = REEL_SYMBOLS[index];
        return currentSymbol;
    }

    public Symbol getCurrentSymbol() {
        return currentSymbol;
    }

    /** Imposta il simbolo corrente (utile per i test). */
    void setCurrentSymbol(Symbol symbol) {
        this.currentSymbol = symbol;
    }
}
