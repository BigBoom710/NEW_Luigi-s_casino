package it.casino.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mazzo di 52 carte. Può essere mescolato e distribuito.
 */
public class Deck {

    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>(52);
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /** Mescola il mazzo. */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Pesca la carta in cima al mazzo.
     *
     * @throws IllegalStateException se il mazzo è vuoto
     */
    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Il mazzo è esaurito.");
        }
        return cards.remove(cards.size() - 1);
    }

    /** Restituisce il numero di carte rimanenti. */
    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
