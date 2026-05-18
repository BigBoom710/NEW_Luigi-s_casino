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
        // Contiamo le frequenze usando un semplice array
        int[] frequenze = new int[SIMBOLI.length];
        for (String carta : manoLuigi) {
            frequenze[indiceSimbolo(carta)]++;
        }
        
        boolean[] daScartare = new boolean[DIMENSIONE];
        for (int i = 0; i < DIMENSIONE; i++) {
            if (frequenze[indiceSimbolo(manoLuigi[i])] == 1) {
                daScartare[i] = true;
            }
        }
        scarta(daScartare, manoLuigi);
    }

    int[] valuta(String[] mano) {
        // 1. Contiamo quante volte appare ogni simbolo nella mano
        int[] frequenze = new int[SIMBOLI.length];
        for (String carta : mano) {
            frequenze[indiceSimbolo(carta)]++;
        }

        int primaFrequenza = 0;
        int secondaFrequenza = 0;
        int migliorIndice = 0;

        // 2. Troviamo la frequenza massima e la seconda massima con un solo ciclo.
        // Scorriamo l'array al contrario (da 5 a 0) così, in caso di parità di frequenza,
        // il simbolo con l'indice più alto (più forte) prenderà automaticamente il sopravvento.
        for (int i = SIMBOLI.length - 1; i >= 0; i--) {
            int f = frequenze[i];
            if (f > primaFrequenza) {
                secondaFrequenza = primaFrequenza;
                primaFrequenza = f;
                migliorIndice = i;
            } else if (f > secondaFrequenza) {
                secondaFrequenza = f;
            }
        }

        // 3. Calcolo del punteggio identico a prima
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

        return new int[]{ punteggio, migliorIndice };
    }

    int indiceSimbolo(String simbolo) {
        for (int i = 0; i < SIMBOLI.length; i++) {
            if (SIMBOLI[i].equals(simbolo)) {
                return i;
            }
        }
        return 0;
    }

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