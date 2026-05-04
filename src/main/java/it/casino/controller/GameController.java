package it.casino.controller;

import it.casino.model.Player;
import it.casino.view.GameView;

/**
 * Controller principale del casino.
 * Gestisce il ciclo di gioco e il menu principale.
 */
public class GameController {

    private static final int MENU_SLOT      = 1;
    private static final int MENU_BLACKJACK = 2;
    private static final int MENU_ROULETTE  = 3;
    private static final int MENU_EXIT      = 4;

    private static final int STARTING_COINS = 100;

    private final GameView view;
    private final Player player;

    private final SlotMachineController slotController;
    private final BlackjackController   blackjackController;
    private final RouletteController    rouletteController;

    public GameController(GameView view, String playerName) {
        this.view   = view;
        this.player = new Player(playerName, STARTING_COINS);

        this.slotController      = new SlotMachineController(view);
        this.blackjackController = new BlackjackController(view);
        this.rouletteController  = new RouletteController(view);
    }

    /** Avvia il loop principale del gioco. */
    public void start() {
        view.showWelcome();
        view.showPlayerStatus(player);

        boolean running = true;
        while (running) {
            if (player.getCoins() <= 0) {
                view.showError("Hai esaurito le monete! Partita terminata.");
                break;
            }

            int choice = view.showMainMenu();
            switch (choice) {
                case MENU_SLOT      -> slotController.play(player);
                case MENU_BLACKJACK -> blackjackController.play(player);
                case MENU_ROULETTE  -> rouletteController.play(player);
                case MENU_EXIT      -> running = false;
            }
        }

        view.showGameOver(player);
    }

    public Player getPlayer() {
        return player;
    }
}
