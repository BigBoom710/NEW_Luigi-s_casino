class Controller {
    private final Model gameModel;
    private final Viewer gameView;

    Controller() {
        this.gameModel = new Model();
        this.gameView = new Viewer();
    }

    void startGame() {
        try {
            gameView.showWelcome();
            String playerName = gameView.askPlayerName();

            while (gameModel.hasCardsForRound()) {
                gameView.waitForRoundStart(gameModel.getNextRoundNumber(), playerName);
                Model.RoundResult roundResult = gameModel.playRound();
                gameView.showRoundResult(playerName, roundResult);
            }

            gameView.showFinalResult(gameModel);
        } finally {
            gameView.close();
        }
    }

    public static void main(String[] args) {
        new Controller().startGame();
    }
}
