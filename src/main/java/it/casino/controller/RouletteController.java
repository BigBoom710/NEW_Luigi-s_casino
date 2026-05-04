package it.casino.controller;

import it.casino.model.Player;
import it.casino.model.RouletteGame;
import it.casino.view.GameView;

/**
 * Controller per la Roulette.
 */
public class RouletteController {

    private final GameView view;

    public RouletteController(GameView view) {
        this.view = view;
    }

    /**
     * Avvia un giro della roulette.
     *
     * @param player il giocatore corrente
     */
    public void play(Player player) {
        view.showMessage("=== ROULETTE ===");
        view.showPlayerStatus(player);

        int bet = view.askRouletteBet(player.getCoins());
        player.deductCoins(bet);

        RouletteGame.BetType betType = view.askRouletteBetType();
        int chosenNumber = 0;
        if (betType == RouletteGame.BetType.NUMERO) {
            chosenNumber = view.askRouletteNumber();
        }

        RouletteGame roulette = new RouletteGame();
        int result = roulette.spin();
        boolean won = roulette.wins(betType, chosenNumber);
        int winnings = roulette.calculateWinnings(bet, betType, chosenNumber);

        player.addCoins(winnings);

        view.showRouletteResult(result, betType, chosenNumber, won, winnings);
        view.showPlayerStatus(player);
    }
}
