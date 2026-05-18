import java.util.*;

public class GameModel {

    static final String[] SIMBOLI    = { "Stella", "Mario", "Luigi", "Fiore", "Fungo", "Nuvola" };
    static final String[] COMBINAZIONI = { "Niente", "Coppia", "Doppia coppia", "Tris", "Full", "Poker", "5 uguali" };
    static final int DIMENSIONE = 5;

    String[] manoGiocatore = new String[DIMENSIONE];
    String[] manoLuigi     = new String[DIMENSIONE];
    List<String> mazzo = new ArrayList<>();

    void distribuisci() {
        mazzo.clear();
        for (int copia = 0; copia < 5; copia++) {
            for (String simbolo : SIMBOLI) {
                mazzo.add(simbolo);
            }
        }
        Collections.shuffle(mazzo);
        for (int i = 0; i < DIMENSIONE; i++) {
            manoGiocatore[i] = mazzo.remove(0);
            manoLuigi[i]     = mazzo.remove(0);
        }
    }

    void scarta(boolean[] daScartare, String[] mano) {
        for (int i = 0; i < DIMENSIONE; i++) {
            if (daScartare[i]) {
                mano[i] = mazzo.remove(0);
            }
        }
    }

    void mossaLuigi() {
        Map<String, Integer> conteggio = new HashMap<>();
        for (String carta : manoLuigi) {
            conteggio.merge(carta, 1, Integer::sum);
        }
        boolean[] daScartare = new boolean[DIMENSIONE];
        for (int i = 0; i < DIMENSIONE; i++) {
            if (conteggio.get(manoLuigi[i]) == 1) {
                daScartare[i] = true;
            }
        }
        scarta(daScartare, manoLuigi);
    }

    int[] valuta(String[] mano) {
        Map<String, Integer> conteggio = new HashMap<>();
        for (String carta : mano) {
            conteggio.merge(carta, 1, Integer::sum);
        }

        List<Map.Entry<String, Integer>> voci = new ArrayList<>(conteggio.entrySet());
        voci.sort((a, b) -> {
            if (b.getValue() != a.getValue()) {
                return b.getValue() - a.getValue();
            }
            return indiceSimbolo(b.getKey()) - indiceSimbolo(a.getKey());
        });

        int primaFrequenza  = voci.get(0).getValue();
        int secondaFrequenza = voci.size() > 1 ? voci.get(1).getValue() : 0;

        int punteggio;
        if (primaFrequenza == 5) {
            punteggio = 6;
        } else if (primaFrequenza == 4) {
            punteggio = 5;
        } else if (primaFrequenza == 3 && secondaFrequenza == 2) {
            punteggio = 4;
        } else if (primaFrequenza == 3) {
            punteggio = 3;
        } else if (primaFrequenza == 2 && secondaFrequenza == 2) {
            punteggio = 2;
        } else if (primaFrequenza == 2) {
            punteggio = 1;
        } else {
            punteggio = 0;
        }

        return new int[]{ punteggio, indiceSimbolo(voci.get(0).getKey()) };
    }

    int indiceSimbolo(String simbolo) {
        for (int i = 0; i < SIMBOLI.length; i++) {
            if (SIMBOLI[i].equals(simbolo)) {
                return i;
            }
        }
        return 0;
    }

    // 1 = vince giocatore, -1 = vince luigi, 0 = pari
    int risultato() {
        int[] punteggioGiocatore = valuta(manoGiocatore);
        int[] punteggioLuigi     = valuta(manoLuigi);
        if (punteggioGiocatore[0] != punteggioLuigi[0]) {
            return Integer.compare(punteggioGiocatore[0], punteggioLuigi[0]);
        }
        return Integer.compare(punteggioGiocatore[1], punteggioLuigi[1]);
    }

    public static void main(String[] args) {
        GameModel gioco = new GameModel();
        Scanner tastiera = new Scanner(System.in);

        while (true) {
            System.out.println("\n[Invio] gioca  [q] esci");
            if ("q".equalsIgnoreCase(tastiera.nextLine())) {
                break;
            }

            gioco.distribuisci();
            System.out.println("La tua mano: " + Arrays.toString(gioco.manoGiocatore));
            System.out.println("Carte da scartare (es: 1 3) o invio per nessuna:");

            boolean[] daScartare = new boolean[DIMENSIONE];
            for (String tasto : tastiera.nextLine().trim().split("\\s+")) {
                try {
                    daScartare[Integer.parseInt(tasto) - 1] = true;
                } catch (Exception errore) {
                    // input non valido, ignorato
                }
            }

            gioco.scarta(daScartare, gioco.manoGiocatore);
            gioco.mossaLuigi();

            int esito = gioco.risultato();
            System.out.println("Tu    : " + Arrays.toString(gioco.manoGiocatore) + " -> " + COMBINAZIONI[gioco.valuta(gioco.manoGiocatore)[0]]);
            System.out.println("Luigi : " + Arrays.toString(gioco.manoLuigi)     + " -> " + COMBINAZIONI[gioco.valuta(gioco.manoLuigi)[0]]);

            if (esito > 0) {
                System.out.println("VINCI!");
            } else if (esito < 0) {
                System.out.println("PERDI!");
            } else {
                System.out.println("PARI");
            }
        }
    }
}