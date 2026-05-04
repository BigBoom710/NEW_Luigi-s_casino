package it.casino.view;

import it.casino.model.BlackjackGame;
import it.casino.model.Card;
import it.casino.model.Player;
import it.casino.model.RouletteGame;
import it.casino.model.Symbol;

import java.util.List;

/**
 * Interfaccia della vista (View) nel pattern MVC.
 * Tutte le operazioni di output verso l'utente passano da qui.
 */
public interface GameView {

    /** Mostra il messaggio di benvenuto. */
    void showWelcome();

    /** Mostra il menu principale e restituisce la scelta dell'utente. */
    int showMainMenu();

    /** Mostra lo stato corrente del giocatore. */
    void showPlayerStatus(Player player);

    // --- Slot Machine ---

    /** Chiede la puntata per la slot machine. */
    int askSlotBet(int maxCoins);

    /** Mostra il risultato di un giro della slot machine. */
    void showSlotResult(Symbol[] symbols, int winnings);

    // --- Blackjack ---

    /** Chiede la puntata per il blackjack. */
    int askBlackjackBet(int maxCoins);

    /** Mostra le mani iniziali. */
    void showBlackjackHands(List<Card> playerHand, Card dealerVisible, int playerValue);

    /** Chiede al giocatore se vuole pescare (H) o stare (S). */
    String askBlackjackAction();

    /** Mostra il risultato finale del blackjack. */
    void showBlackjackResult(List<Card> playerHand, List<Card> dealerHand,
                             int playerValue, int dealerValue,
                             BlackjackGame.GameState state, int winnings);

    // --- Roulette ---

    /** Chiede la puntata per la roulette. */
    int askRouletteBet(int maxCoins);

    /** Chiede il tipo di puntata per la roulette. */
    RouletteGame.BetType askRouletteBetType();

    /** Chiede il numero (usato solo per BetType.NUMERO). */
    int askRouletteNumber();

    /** Mostra il risultato della roulette. */
    void showRouletteResult(int result, RouletteGame.BetType betType,
                            int betNumber, boolean won, int winnings);

    // --- Messaggi generici ---

    /** Mostra un messaggio informativo. */
    void showMessage(String message);

    /** Mostra un messaggio di errore. */
    void showError(String message);

    /** Mostra il messaggio di fine gioco. */
    void showGameOver(Player player);
}
