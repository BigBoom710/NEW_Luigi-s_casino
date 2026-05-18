import javax.swing.SwingUtilities;
import java.awt.Color;

public class GameController {

    private final GameModel model;
    private final GameView  view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view  = view;
        registerListeners();
        refresh();
    }

    private void registerListeners() {
        view.addActionListener(e -> handleAction());

        for (int i = 0; i < GameModel.HAND_SIZE; i++) {
            final int idx = i;
            view.addScartaListener(i, e -> {
                model.toggleDiscard(idx);
                view.showPlayerCard(idx, model.getPlayerCard(idx), model.isDiscarded(idx));
            });
        }
    }

    private void handleAction() {
        switch (model.getState()) {
            case IDLE:        model.deal();           break;
            case PLAYER_TURN: model.drawAndResolve(); break;
            case RESULT:      model.nextRound();      break;
        }
        refresh();
    }

    private void refresh() {
        switch (model.getState()) {

            case IDLE:
                for (int i = 0; i < GameModel.HAND_SIZE; i++) {
                    view.showPlayerCard(i, null, false);
                    view.showLuigiCard(i, "");
                }
                view.setScartaEnabled(false);
                view.setActionLabel("Inizia");
                view.clearBanner();
                view.resetHandNames();
                break;

            case PLAYER_TURN:
                for (int i = 0; i < GameModel.HAND_SIZE; i++) {
                    view.showPlayerCard(i, model.getPlayerCard(i), model.isDiscarded(i));
                    view.showLuigiCard(i, null);  // coperta
                }
                view.setScartaEnabled(true);
                view.setActionLabel("Pesca");
                view.clearBanner();
                view.resetHandNames();
                break;

            case RESULT:
                String[] sortedPlayer = model.getSortedPlayerHand();
                String[] sortedLuigi  = model.getSortedLuigiHand();
                for (int i = 0; i < GameModel.HAND_SIZE; i++) {
                    view.showPlayerCard(i, sortedPlayer[i], false);
                    view.showLuigiCard(i, sortedLuigi[i]);
                }
                view.setScartaEnabled(false);
                view.setActionLabel("Ancora");
                view.setPlayerHandName(model.handName(model.getPlayerResult()));
                view.setLuigiHandName(model.handName(model.getLuigiResult()));

                int out = model.getOutcome();
                if (out > 0)
                    view.showBanner("HAI VINTO!", new Color(0, 100, 0), new Color(200, 255, 200));
                else if (out < 0)
                    view.showBanner("HAI PERSO!", new Color(130, 0, 0), new Color(255, 200, 200));
                else
                    view.showBanner("PAREGGIO", new Color(80, 60, 0), new Color(255, 240, 180));
                break;
        }

        view.updateScore(model.getWins(), model.getLosses(), model.getDraws());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            GameView  view  = new GameView();
            new GameController(model, view);
            view.setVisible(true);
        });
    }
}
