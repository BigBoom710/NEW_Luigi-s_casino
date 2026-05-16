import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

class model {
    private static final int MAX_ROUNDS = 5;

    private final Deque<Integer> deck = new ArrayDeque<>();
    private int playerScore;
    private int luigiScore;
    private int round;

    model() {
        List<Integer> cards = new ArrayList<>();
        for (int value = 1; value <= 13; value++) {
            for (int suit = 0; suit < 4; suit++) {
                cards.add(value);
            }
        }
        Collections.shuffle(cards);
        deck.addAll(cards);
    }

    boolean hasCardsForRound() {
        return round < MAX_ROUNDS && deck.size() >= 2;
    }

    RoundResult playRound() {
        int playerCard = deck.removeFirst();
        int luigiCard = deck.removeFirst();
        round++;

        String winner;
        if (playerCard > luigiCard) {
            playerScore++;
            winner = "Giocatore";
        } else if (luigiCard > playerCard) {
            luigiScore++;
            winner = "Luigi";
        } else {
            winner = "Pareggio";
        }

        return new RoundResult(round, playerCard, luigiCard, winner, playerScore, luigiScore);
    }

    String getFinalWinner() {
        if (playerScore > luigiScore) {
            return "Hai battuto Luigi!";
        }
        if (luigiScore > playerScore) {
            return "Luigi vince questa volta!";
        }
        return "Parità perfetta!";
    }

    int getNextRoundNumber() {
        return round + 1;
    }

    static class RoundResult {
        private final int round;
        private final int playerCard;
        private final int luigiCard;
        private final String winner;
        private final int playerScore;
        private final int luigiScore;

        RoundResult(int round, int playerCard, int luigiCard, String winner, int playerScore, int luigiScore) {
            this.round = round;
            this.playerCard = playerCard;
            this.luigiCard = luigiCard;
            this.winner = winner;
            this.playerScore = playerScore;
            this.luigiScore = luigiScore;
        }

        int getRound() {
            return round;
        }

        int getPlayerCard() {
            return playerCard;
        }

        int getLuigiCard() {
            return luigiCard;
        }

        String getWinner() {
            return winner;
        }

        int getPlayerScore() {
            return playerScore;
        }

        int getLuigiScore() {
            return luigiScore;
        }
    }
}
