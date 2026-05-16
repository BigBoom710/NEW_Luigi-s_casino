class controller {
    private final model gameModel;
    private final viewer gameView;

    controller() {
        this.gameModel = new model();
        this.gameView = new viewer();
    }

    void startGame() {
        gameView.showWelcome();
        String playerName = gameView.askPlayerName();

        while (gameModel.hasCardsForRound()) {
            gameView.waitForRoundStart(gameModel.getNextRoundNumber(), playerName);
            model.RoundResult roundResult = gameModel.playRound();
            gameView.showRoundResult(playerName, roundResult);
        }

        gameView.showFinalResult(playerName, gameModel);
    }

    public static void main(String[] args) {
        new controller().startGame();
    }
}
