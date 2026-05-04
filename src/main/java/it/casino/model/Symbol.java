package it.casino.model;

/**
 * Simboli presenti sui rulli della slot machine.
 * Il moltiplicatore rappresenta la vincita per linea completata.
 */
public enum Symbol {

    CILIEGIA("🍒", 2),
    ARANCIA("🍊", 3),
    LIMONE("🍋", 4),
    STELLA("⭐", 6),
    BAR("BAR", 10),
    LUIGI("L", 20),
    SETTE("7", 50);

    private final String display;
    private final int multiplier;

    Symbol(String display, int multiplier) {
        this.display = display;
        this.multiplier = multiplier;
    }

    public String getDisplay() {
        return display;
    }

    public int getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        return display;
    }
}
