package it.casino.model;

/**
 * Rappresenta una carta da gioco con seme e valore.
 */
public class Card {

    public enum Suit {
        CUORI("♥"), QUADRI("♦"), FIORI("♣"), PICCHE("♠");

        private final String symbol;

        Suit(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum Rank {
        DUE(2, "2"),   TRE(3, "3"),   QUATTRO(4, "4"),
        CINQUE(5, "5"), SEI(6, "6"),  SETTE(7, "7"),
        OTTO(8, "8"),  NOVE(9, "9"),  DIECI(10, "10"),
        JACK(10, "J"), DONNA(10, "Q"), RE(10, "K"), ASSO(11, "A");

        private final int value;
        private final String label;

        Rank(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    /** Restituisce il valore della carta (l'Asso può valere 1 o 11, gestito dalla mano). */
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank.getLabel() + suit.getSymbol();
    }
}
