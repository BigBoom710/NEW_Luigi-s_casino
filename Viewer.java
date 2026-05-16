import java.util.Scanner;

class Viewer {
    private static final String DEFAULT_PLAYER_NAME = "Giocatore";
    private final Scanner scanner = new Scanner(System.in);

    void showWelcome() {
        System.out.println("=== Luigi's Casino - Gioco di Carte Base ===");
        System.out.println("Regole: 5 round, carta più alta vince il round.");
    }

    String askPlayerName() {
        System.out.print("Inserisci il tuo nome: ");
        String name = scanner.nextLine().trim();
        return name.isEmpty() ? DEFAULT_PLAYER_NAME : name;
    }

    void waitForRoundStart(int round, String playerName) {
        System.out.println();
        System.out.println("Round " + round + " - " + playerName + " vs Luigi");
        System.out.print("Premi INVIO per pescare le carte...");
        scanner.nextLine();
    }

    void showRoundResult(String playerName, Model.RoundResult result) {
        System.out.println(playerName + " pesca: " + result.getPlayerCard());
        System.out.println("Luigi pesca: " + result.getLuigiCard());

        switch (result.getWinner()) {
            case PLAYER -> System.out.println("Esito round: Vince " + playerName + "!");
            case LUIGI -> System.out.println("Esito round: Vince Luigi!");
            case TIE -> System.out.println("Esito round: Pareggio!");
        }

        System.out.println("Punteggio -> " + playerName + ": " + result.getPlayerScore() + " | Luigi: " + result.getLuigiScore());
    }

    void showFinalResult(Model game) {
        System.out.println();
        System.out.println("=== Risultato finale ===");
        System.out.println(game.getFinalWinner());
    }

    void close() {
        scanner.close();
    }
}
