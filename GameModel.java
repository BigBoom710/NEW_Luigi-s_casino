import java.util.*;

public class GameModel {

    public enum State { IDLE, PLAYER_TURN, RESULT }

    // Peggiore → migliore: Stella < Mario < Luigi < Fiore < Fungo < Nuvola
    public static final String[] CHARACTERS = { "Stella", "Mario", "Luigi", "Fiore", "Fungo", "Nuvola" };

    public static final String[] HAND_NAMES = {
        "Niente", "Coppia", "Doppia coppia", "Tris", "Full house", "Poker", "Cinque uguali"
    };

    public static final int HAND_SIZE = 5;

    private State    state    = State.IDLE;
    private String[] playerHand = new String[HAND_SIZE];
    private String[] luigiHand  = new String[HAND_SIZE];
    private boolean[] discard   = new boolean[HAND_SIZE];
    private int[] playerResult, luigiResult;
    private int outcome;   // 1 = vince player, -1 = vince luigi, 0 = pareggio
    private int wins, losses, draws;
    private List<String> deck = new ArrayList<>();

    // ── Mazzo ────────────────────────────────────────────────────────────────

    private void buildDeck() {
        deck.clear();
        for (int k = 0; k < 5; k++)          // 5 copie × 6 simboli = 30 carte
            for (String ch : CHARACTERS)
                deck.add(ch);
        Collections.shuffle(deck);
    }

    private String draw() { return deck.remove(deck.size() - 1); }

    // ── Azioni ───────────────────────────────────────────────────────────────

    public void deal() {
        buildDeck();
        for (int i = 0; i < HAND_SIZE; i++) {
            playerHand[i] = draw();
            luigiHand[i]  = draw();
            discard[i]    = false;
        }
        state = State.PLAYER_TURN;
    }

    public void toggleDiscard(int i) {
        if (state == State.PLAYER_TURN) discard[i] = !discard[i];
    }

    public void drawAndResolve() {
        if (state != State.PLAYER_TURN) return;

        // Giocatore pesca le carte scartate
        for (int i = 0; i < HAND_SIZE; i++)
            if (discard[i]) playerHand[i] = draw();

        // Luigi scarta i singoli (strategia ufficiale del gioco originale)
        for (int i = 0; i < HAND_SIZE; i++)
            if (luigiShouldDiscard(i)) luigiHand[i] = draw();

        // Valuta e confronta
        playerResult = evaluate(playerHand);
        luigiResult  = evaluate(luigiHand);
        outcome = compare(playerResult, luigiResult);

        if      (outcome > 0) wins++;
        else if (outcome < 0) losses++;
        else                  draws++;

        state = State.RESULT;
    }

    // Vera strategia di Luigi: scarta tutte le carte che compaiono una sola volta
    private boolean luigiShouldDiscard(int i) {
        int count = 0;
        for (String c : luigiHand)
            if (c.equals(luigiHand[i])) count++;
        return count == 1;
    }

    // Restituisce {rankMano, rankPrimario, rankSecondario} per confronto
    private int[] evaluate(String[] hand) {
        Map<String, Integer> freq = new HashMap<>();
        for (String c : hand) freq.merge(c, 1, Integer::sum);

        // Ordina per frequenza desc, poi per valore carta desc
        List<Map.Entry<String, Integer>> e = new ArrayList<>(freq.entrySet());
        e.sort((a, b) -> {
            int d = Integer.compare(b.getValue(), a.getValue());
            return d != 0 ? d : Integer.compare(cardRank(b.getKey()), cardRank(a.getKey()));
        });

        int f0 = e.get(0).getValue();
        int f1 = e.size() > 1 ? e.get(1).getValue() : 0;
        int r0 = cardRank(e.get(0).getKey());
        int r1 = e.size() > 1 ? cardRank(e.get(1).getKey()) : 0;

        if (f0 == 5)             return new int[]{ 6, r0, 0  };
        if (f0 == 4)             return new int[]{ 5, r0, 0  };
        if (f0 == 3 && f1 == 2)  return new int[]{ 4, r0, r1 };
        if (f0 == 3)             return new int[]{ 3, r0, 0  };
        if (f0 == 2 && f1 == 2)  return new int[]{ 2, r0, r1 };
        if (f0 == 2)             return new int[]{ 1, r0, 0  };
        return new int[]{ 0, r0, 0 };  // niente: valore carta più alta
    }

    // Confronto lessicografico: positivo = a vince
    private int compare(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i]) return Integer.compare(a[i], b[i]);
        return 0;
    }

    private int cardRank(String name) {
        for (int i = 0; i < CHARACTERS.length; i++)
            if (CHARACTERS[i].equals(name)) return i;
        return 0;
    }

    public void nextRound() { state = State.IDLE; }

    // ── Getter ────────────────────────────────────────────────────────────────

    public State   getState()            { return state; }
    public String  getPlayerCard(int i)  { return playerHand[i]; }
    public String  getLuigiCard(int i)   { return luigiHand[i]; }

    // Restituisce le carte ordinate per gruppo (freq desc, poi valore desc)
    // Usato dalla View a fine partita per mostrare coppie/tris raggruppati
    public String[] sortedHand(String[] hand) {
        Map<String, Integer> freq = new HashMap<>();
        for (String c : hand) freq.merge(c, 1, Integer::sum);
        List<String> sorted = new ArrayList<>(Arrays.asList(hand));
        sorted.sort((a, b) -> {
            int d = Integer.compare(freq.get(b), freq.get(a));
            return d != 0 ? d : Integer.compare(cardRank(b), cardRank(a));
        });
        return sorted.toArray(new String[0]);
    }

    public String[] getSortedPlayerHand() { return sortedHand(playerHand); }
    public String[] getSortedLuigiHand()  { return sortedHand(luigiHand); }
    public boolean isDiscarded(int i)    { return discard[i]; }
    public int[]   getPlayerResult()     { return playerResult; }
    public int[]   getLuigiResult()      { return luigiResult; }
    public int     getOutcome()          { return outcome; }
    public int     getWins()             { return wins; }
    public int     getLosses()           { return losses; }
    public int     getDraws()            { return draws; }
    public String  handName(int[] r)     { return HAND_NAMES[r[0]]; }
}
