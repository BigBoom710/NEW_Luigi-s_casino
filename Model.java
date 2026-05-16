import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

class Model {
    private static final int MAX_ROUNDS = 5;
    private static final int MAX_CARD_VALUE = 13;
    static final String DEFAULT_PLAYER_NAME = "Giocatore";

    private final Deque<Integer> deck = new ArrayDeque<>();
    private int playerScore;
    private int luigiScore;
    private int round;

    Model() {
        List<Integer> cards = new ArrayList<>();
        for (int value = 1; value <= MAX_CARD_VALUE; value++) {
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

        RoundResult.RoundWinner winner;
        if (playerCard > luigiCard) {
            playerScore++;
            winner = RoundResult.RoundWinner.PLAYER;
        } else if (luigiCard > playerCard) {
            luigiScore++;
            winner = RoundResult.RoundWinner.LUIGI;
        } else {
            winner = RoundResult.RoundWinner.TIE;
        }

        return new RoundResult(playerCard, luigiCard, winner, playerScore, luigiScore);
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
        enum RoundWinner { PLAYER, LUIGI, TIE }

        private final int playerCard;
        private final int luigiCard;
        private final RoundWinner winner;
        private final int playerScore;
        private final int luigiScore;

        RoundResult(int playerCard, int luigiCard, RoundWinner winner, int playerScore, int luigiScore) {
            this.playerCard = playerCard;
            this.luigiCard = luigiCard;
            this.winner = winner;
            this.playerScore = playerScore;
            this.luigiScore = luigiScore;
        }

        int getPlayerCard() {
            return playerCard;
        }

        int getLuigiCard() {
            return luigiCard;
        }

        RoundWinner getWinner() {
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
