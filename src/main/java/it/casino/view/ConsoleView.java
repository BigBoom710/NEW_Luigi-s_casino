package it.casino.view;

import it.casino.model.BlackjackGame;
import it.casino.model.Card;
import it.casino.model.Player;
import it.casino.model.RouletteGame;
import it.casino.model.Symbol;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Implementazione console della {@link GameView}.
 * Gestisce tutto l'I/O testuale con l'utente.
 */
public class ConsoleView implements GameView {

    private static final String SEPARATOR =
        "══════════════════════════════════════════════════";
    private static final String LUIGI_BANNER =
        """
        ╔══════════════════════════════════════════════════╗
        ║          🍀  LUIGI'S CASINO  🍀                  ║
        ║      La Tua Fortuna Ti Aspetta!                  ║
        ╚══════════════════════════════════════════════════╝
        """;

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void showWelcome() {
        System.out.println(LUIGI_BANNER);
    }

    @Override
    public int showMainMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("  MENU PRINCIPALE");
        System.out.println(SEPARATOR);
        System.out.println("  1. 🎰 Slot Machine");
        System.out.println("  2. 🃏 Blackjack");
        System.out.println("  3. 🎡 Roulette");
        System.out.println("  4. 🚪 Esci");
        System.out.println(SEPARATOR);
        System.out.print("  Scelta: ");
        return readInt(1, 4);
    }

    @Override
    public void showPlayerStatus(Player player) {
        System.out.printf("%n  👤 %s  |  💰 Monete: %d%n%n",
            player.getName(), player.getCoins());
    }

    // ─── Slot Machine ──────────────────────────────────────────────────────────

    @Override
    public int askSlotBet(int maxCoins) {
        System.out.printf("  Puntata (1-%d): ", maxCoins);
        return readInt(1, maxCoins);
    }

    @Override
    public void showSlotResult(Symbol[] symbols, int winnings) {
        System.out.println("\n  ┌─────────────────┐");
        System.out.printf("  │  %s  │  %s  │  %s  │%n",
            padSymbol(symbols[0].getDisplay()),
            padSymbol(symbols[1].getDisplay()),
            padSymbol(symbols[2].getDisplay()));
        System.out.println("  └─────────────────┘");
        if (winnings > 0) {
            System.out.printf("%n  🎉 Hai vinto %d monete!%n", winnings);
        } else {
            System.out.println("\n  😔 Nessuna vincita.");
        }
    }

    // ─── Blackjack ─────────────────────────────────────────────────────────────

    @Override
    public int askBlackjackBet(int maxCoins) {
        System.out.printf("  Puntata (1-%d): ", maxCoins);
        return readInt(1, maxCoins);
    }

    @Override
    public void showBlackjackHands(List<Card> playerHand, Card dealerVisible, int playerValue) {
        System.out.println("\n  --- BLACKJACK ---");
        System.out.printf("  Banco:    [ %s ] [ ?? ]%n", dealerVisible);
        System.out.printf("  Tu:       %s  (totale: %d)%n",
            formatHand(playerHand), playerValue);
    }

    @Override
    public String askBlackjackAction() {
        System.out.print("\n  [H] Pesca  [S] Stai: ");
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("H") || input.equals("S")) {
                return input;
            }
            System.out.print("  Scelta non valida. [H] Pesca  [S] Stai: ");
        }
    }

    @Override
    public void showBlackjackResult(List<Card> playerHand, List<Card> dealerHand,
                                    int playerValue, int dealerValue,
                                    BlackjackGame.GameState state, int winnings) {
        System.out.println("\n  --- RISULTATO BLACKJACK ---");
        System.out.printf("  Banco:    %s  (totale: %d)%n", formatHand(dealerHand), dealerValue);
        System.out.printf("  Tu:       %s  (totale: %d)%n", formatHand(playerHand), playerValue);

        String outcome = switch (state) {
            case PLAYER_WIN -> "🎉 Hai vinto! +" + winnings + " monete";
            case DEALER_WIN -> "😔 Ha vinto il banco.";
            case DRAW       -> "🤝 Pareggio! Ti vengono restituite " + winnings + " monete.";
            default         -> "";
        };
        System.out.println("  " + outcome);
    }

    // ─── Roulette ──────────────────────────────────────────────────────────────

    @Override
    public int askRouletteBet(int maxCoins) {
        System.out.printf("  Puntata (1-%d): ", maxCoins);
        return readInt(1, maxCoins);
    }

    @Override
    public RouletteGame.BetType askRouletteBetType() {
        System.out.println("\n  Tipo di puntata:");
        System.out.println("  1. Numero pieno (0-36)");
        System.out.println("  2. Rosso");
        System.out.println("  3. Nero");
        System.out.println("  4. Pari");
        System.out.println("  5. Dispari");
        System.out.println("  6. Bassa (1-18)");
        System.out.println("  7. Alta (19-36)");
        System.out.print("  Scelta: ");
        int choice = readInt(1, 7);
        return RouletteGame.BetType.values()[choice - 1];
    }

    @Override
    public int askRouletteNumber() {
        System.out.print("  Numero (0-36): ");
        return readInt(0, 36);
    }

    @Override
    public void showRouletteResult(int result, RouletteGame.BetType betType,
                                   int betNumber, boolean won, int winnings) {
        System.out.printf("%n  🎡 La ruota si ferma su: %d%n", result);
        if (won) {
            System.out.printf("  🎉 Hai vinto! +%d monete!%n", winnings);
        } else {
            System.out.println("  😔 Hai perso.");
        }
    }

    // ─── Messaggi generici ─────────────────────────────────────────────────────

    @Override
    public void showMessage(String message) {
        System.out.println("  " + message);
    }

    @Override
    public void showError(String message) {
        System.out.println("  ⚠️  " + message);
    }

    @Override
    public void showGameOver(Player player) {
        System.out.println("\n" + SEPARATOR);
        System.out.printf("  Partita terminata. %s ha %d monete.%n",
            player.getName(), player.getCoins());
        System.out.println("  Grazie per aver giocato a Luigi's Casino! Arrivederci!");
        System.out.println(SEPARATOR);
    }

    // ─── Utilities ─────────────────────────────────────────────────────────────

    private int readInt(int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  Inserisci un numero tra %d e %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("  Input non valido. Inserisci un numero tra %d e %d: ", min, max);
            }
        }
    }

    private String formatHand(List<Card> hand) {
        return hand.stream()
            .map(Card::toString)
            .collect(Collectors.joining(" "));
    }

    private String padSymbol(String symbol) {
        // Note: emoji glyphs are typically 2 columns wide in terminals, so
        // simple String.format padding may not align perfectly on all terminals.
        // This is a known display limitation of console-based emoji rendering.
        return String.format("%-3s", symbol);
    }
}
