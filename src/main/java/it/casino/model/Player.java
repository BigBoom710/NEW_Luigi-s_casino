package it.casino.model;

/**
 * Rappresenta il giocatore con nome e saldo di monete.
 */
public class Player {

    private final String name;
    private int coins;

    public Player(String name, int initialCoins) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome del giocatore non può essere vuoto.");
        }
        if (initialCoins < 0) {
            throw new IllegalArgumentException("Il saldo iniziale non può essere negativo.");
        }
        this.name = name;
        this.coins = initialCoins;
    }

    public String getName() {
        return name;
    }

    public int getCoins() {
        return coins;
    }

    /** Aggiunge monete al saldo. */
    public void addCoins(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("L'importo da aggiungere non può essere negativo.");
        }
        this.coins += amount;
    }

    /** Sottrae monete dal saldo; lancia eccezione se insufficienti. */
    public void deductCoins(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("L'importo da sottrarre non può essere negativo.");
        }
        if (amount > coins) {
            throw new IllegalStateException("Monete insufficienti.");
        }
        this.coins -= amount;
    }

    /** Restituisce true se il giocatore ha almeno {@code amount} monete. */
    public boolean canBet(int amount) {
        return amount > 0 && amount <= coins;
    }

    @Override
    public String toString() {
        return String.format("%s (monete: %d)", name, coins);
    }
}
