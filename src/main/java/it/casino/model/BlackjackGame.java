package it.casino.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Logica di gioco del Blackjack (21).
 *
 * <p>Regole semplificate:
 * <ul>
 *   <li>Il giocatore e il banco ricevono 2 carte iniziali.</li>
 *   <li>Il giocatore può pescare (hit) o stare (stand).</li>
 *   <li>Vince chi si avvicina di più a 21 senza superarlo.</li>
 *   <li>Il banco pesca automaticamente fino a 17+.</li>
 *   <li>Il Blackjack naturale (21 con 2 carte) paga 1.5×.</li>
 * </ul>
 */
public class BlackjackGame {

    public enum GameState { IN_PROGRESS, PLAYER_WIN, DEALER_WIN, DRAW }

    private final Deck deck;
    private final List<Card> playerHand;
    private final List<Card> dealerHand;
    private GameState state;
    private boolean playerStood;

    public BlackjackGame() {
        deck = new Deck();
        deck.shuffle();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        state = GameState.IN_PROGRESS;
        playerStood = false;

        // Distribuzione iniziale
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());
        playerHand.add(deck.deal());
        dealerHand.add(deck.deal());

        checkInitialBlackjack();
    }

    private void checkInitialBlackjack() {
        boolean playerBlackjack = handValue(playerHand) == 21 && playerHand.size() == 2;
        boolean dealerBlackjack = handValue(dealerHand) == 21 && dealerHand.size() == 2;

        if (playerBlackjack && dealerBlackjack) {
            state = GameState.DRAW;
        } else if (playerBlackjack) {
            state = GameState.PLAYER_WIN;
        } else if (dealerBlackjack) {
            state = GameState.DEALER_WIN;
        }
    }

    /** Il giocatore pesca una carta. */
    public void playerHit() {
        if (state != GameState.IN_PROGRESS) {
            return;
        }
        playerHand.add(deck.deal());
        if (handValue(playerHand) > 21) {
            state = GameState.DEALER_WIN; // sballato
        }
    }

    /** Il giocatore si ferma; il banco gioca automaticamente. */
    public void playerStand() {
        if (state != GameState.IN_PROGRESS) {
            return;
        }
        playerStood = true;
        dealerPlay();
    }

    private void dealerPlay() {
        while (handValue(dealerHand) < 17) {
            dealerHand.add(deck.deal());
        }
        resolveGame();
    }

    private void resolveGame() {
        int playerVal = handValue(playerHand);
        int dealerVal = handValue(dealerHand);

        if (dealerVal > 21) {
            state = GameState.PLAYER_WIN;
        } else if (playerVal > dealerVal) {
            state = GameState.PLAYER_WIN;
        } else if (dealerVal > playerVal) {
            state = GameState.DEALER_WIN;
        } else {
            state = GameState.DRAW;
        }
    }

    /**
     * Calcola il valore ottimale della mano (gli Assi possono valere 1 o 11).
     */
    public static int handValue(List<Card> hand) {
        int total = 0;
        int aces = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.getRank() == Card.Rank.ASSO) {
                aces++;
            }
        }
        while (total > 21 && aces > 0) {
            total -= 10; // Asso vale 1 invece di 11
            aces--;
        }
        return total;
    }

    /**
     * Calcola la vincita in base alla puntata.
     * Blackjack naturale → 2.5×; vittoria normale → 2×; pareggio → restituisce puntata; sconfitta → 0.
     */
    public int calculateWinnings(int bet) {
        return switch (state) {
            case PLAYER_WIN -> isNaturalBlackjack() ? (int) (bet * 2.5) : bet * 2;
            case DRAW -> bet;
            default -> 0;
        };
    }

    private boolean isNaturalBlackjack() {
        return playerHand.size() == 2 && handValue(playerHand) == 21;
    }

    public List<Card> getPlayerHand() {
        return List.copyOf(playerHand);
    }

    public List<Card> getDealerHand() {
        return List.copyOf(dealerHand);
    }

    /** Restituisce la prima carta visibile del banco (la seconda è coperta). */
    public Card getDealerVisibleCard() {
        return dealerHand.isEmpty() ? null : dealerHand.get(0);
    }

    public GameState getState() {
        return state;
    }

    public boolean isOver() {
        return state != GameState.IN_PROGRESS;
    }

    public boolean hasPlayerStood() {
        return playerStood;
    }
}
