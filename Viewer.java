import java.util.Scanner;

class Viewer {
    private final Scanner scanner = new Scanner(System.in);

    void showWelcome() {
        System.out.println("=== Luigi's Casino - Gioco di Carte Base ===");
        System.out.println("Regole: 5 round, carta più alta vince il round.");
    }

    String askPlayerName() {
        System.out.print("Inserisci il tuo nome: ");
        String name = scanner.nextLine().trim();
        return name.isEmpty() ? Model.DEFAULT_PLAYER_NAME : name;
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

        if ("Pareggio".equals(result.getWinner())) {
            System.out.println("Esito round: Pareggio!");
        } else {
            System.out.println("Esito round: Vince " + result.getWinner() + "!");
        }

        System.out.println("Punteggio -> " + playerName + ": " + result.getPlayerScore() + " | Luigi: " + result.getLuigiScore());
    }

    void showFinalResult(String playerName, Model game) {
        System.out.println();
        System.out.println("=== Risultato finale ===");
        System.out.println(playerName + " " + game.getFinalWinner());
    }
}
