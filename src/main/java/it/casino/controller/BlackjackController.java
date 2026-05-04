package it.casino.controller;

import it.casino.model.BlackjackGame;
import it.casino.model.Card;
import it.casino.model.Player;
import it.casino.view.GameView;

import java.util.List;

/**
 * Controller per il gioco del Blackjack.
 */
public class BlackjackController {

    private final GameView view;

    public BlackjackController(GameView view) {
        this.view = view;
    }

    /**
     * Avvia una partita di Blackjack.
     *
     * @param player il giocatore corrente
     */
    public void play(Player player) {
        view.showMessage("=== BLACKJACK ===");
        view.showPlayerStatus(player);

        int bet = view.askBlackjackBet(player.getCoins());
        player.deductCoins(bet);

        BlackjackGame game = new BlackjackGame();

        // Mani iniziali
        List<Card> playerHand = game.getPlayerHand();
        int playerValue = BlackjackGame.handValue(playerHand);
        view.showBlackjackHands(playerHand, game.getDealerVisibleCard(), playerValue);

        // Se il gioco è già finito (blackjack naturale)
        if (!game.isOver()) {
            runPlayerTurn(game);
        }

        // Risultato finale
        int winnings = game.calculateWinnings(bet);
        player.addCoins(winnings);

        List<Card> finalPlayerHand = game.getPlayerHand();
        List<Card> finalDealerHand = game.getDealerHand();
        view.showBlackjackResult(
            finalPlayerHand,
            finalDealerHand,
            BlackjackGame.handValue(finalPlayerHand),
            BlackjackGame.handValue(finalDealerHand),
            game.getState(),
            winnings
        );
        view.showPlayerStatus(player);
    }

    private void runPlayerTurn(BlackjackGame game) {
        while (!game.isOver()) {
            String action = view.askBlackjackAction();
            if ("H".equals(action)) {
                game.playerHit();
                List<Card> hand = game.getPlayerHand();
                view.showBlackjackHands(hand, game.getDealerVisibleCard(),
                    BlackjackGame.handValue(hand));
            } else {
                game.playerStand();
            }
        }
    }
}
