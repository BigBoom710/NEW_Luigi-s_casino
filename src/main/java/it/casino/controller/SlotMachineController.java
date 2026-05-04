package it.casino.controller;

import it.casino.model.Player;
import it.casino.model.SlotMachine;
import it.casino.model.Symbol;
import it.casino.view.GameView;

/**
 * Controller per la Slot Machine.
 */
public class SlotMachineController {

    private final GameView view;
    private final SlotMachine slotMachine;

    public SlotMachineController(GameView view) {
        this.view = view;
        this.slotMachine = new SlotMachine();
    }

    /**
     * Avvia un giro della slot machine.
     *
     * @param player il giocatore corrente
     */
    public void play(Player player) {
        view.showMessage("=== SLOT MACHINE ===");
        view.showPlayerStatus(player);

        int bet = view.askSlotBet(player.getCoins());
        player.deductCoins(bet);

        int winnings = slotMachine.spin(bet);
        player.addCoins(winnings);

        Symbol[] result = slotMachine.getLastResult();
        view.showSlotResult(result, winnings);

        if (slotMachine.isJackpot()) {
            view.showMessage("🎊 JACKPOT! Tutti e tre i simboli uguali!");
        }

        view.showPlayerStatus(player);
    }
}
