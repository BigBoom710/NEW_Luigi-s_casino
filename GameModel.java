import java.util.*;

/*
 * for/if senza graffe → corpo di una sola istruzione
 * condizione ? a : b  → if (condizione) a; else b;
 * metodo su una riga  → metodo normale con corpo su più righe
 * if (...) return;    → esce subito, il resto esegue solo se falso
 */

public class GameModel {

    static final int ATTESA = 0, TURNO_GIOCATORE = 1, RISULTATO = 2;

    // Peggiore → migliore: Stella < Mario < Luigi < Fiore < Fungo < Nuvola
    public static final String[] SIMBOLI = { "Stella", "Mario", "Luigi", "Fiore", "Fungo", "Nuvola" };

    public static final String[] COMBINAZIONI = {
        "Niente", "Coppia", "Doppia coppia", "Tris", "Full house", "Poker", "Cinque uguali"
    };

    public static final int DIMENSIONE = 5;

    private int stato = ATTESA;
    private String[] manoGiocatore = new String[DIMENSIONE];
    private String[] manoLuigi     = new String[DIMENSIONE];
    private boolean[] daScartare   = new boolean[DIMENSIONE];
    private int[] punteggioGiocatore, punteggioLuigi;
    private int esito;   // 1 = vince giocatore, -1 = vince luigi, 0 = pareggio
    private int vittorie, sconfitte, pareggi;
    private List<String> mazzo = new ArrayList<>();

    private void costruisciMazzo() {
        mazzo.clear();
        for (int copia = 0; copia < 5; copia++)    // 5 copie x 6 simboli = 30 carte
            for (String simbolo : SIMBOLI)
                mazzo.add(simbolo);
        Collections.shuffle(mazzo);
    }

    private String pesca() { return mazzo.remove(mazzo.size() - 1); }

    public void distribuisci() {
        costruisciMazzo();
        for (int i = 0; i < DIMENSIONE; i++) {
            manoGiocatore[i] = pesca();
            manoLuigi[i]     = pesca();
            daScartare[i]    = false;
        }
        stato = TURNO_GIOCATORE;
    }

    public void toggleScarto(int i) {
        if (stato == TURNO_GIOCATORE) daScartare[i] = !daScartare[i];
    }

    public void pescaERisolvi() {
        if (stato != TURNO_GIOCATORE) return;

        for (int i = 0; i < DIMENSIONE; i++)
            if (daScartare[i]) manoGiocatore[i] = pesca();

        for (int i = 0; i < DIMENSIONE; i++)
            if (luigiDeveScartare(i)) manoLuigi[i] = pesca();

        punteggioGiocatore = valuta(manoGiocatore);
        punteggioLuigi     = valuta(manoLuigi);
        esito = confronta(punteggioGiocatore, punteggioLuigi);

        if      (esito > 0) vittorie++;
        else if (esito < 0) sconfitte++;
        else                pareggi++;

        stato = RISULTATO;
    }

    private boolean luigiDeveScartare(int i) {
        int contatore = 0;
        for (String carta : manoLuigi)
            if (carta.equals(manoLuigi[i])) contatore++;
        return contatore == 1;
    }

    private Map<String, Integer> costruisciFrequenze(String[] mano) {
        Map<String, Integer> frequenze = new HashMap<>();
        for (String carta : mano) {
            Integer corrente = frequenze.get(carta);
            frequenze.put(carta, corrente == null ? 1 : corrente + 1);
        }
        return frequenze;
    }

    private int[] valuta(String[] mano) {
        final Map<String, Integer> frequenze = costruisciFrequenze(mano);

        List<Map.Entry<String, Integer>> voci = new ArrayList<>(frequenze.entrySet());
        Collections.sort(voci, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                int differenza = Integer.compare(b.getValue(), a.getValue());
                return differenza != 0 ? differenza : Integer.compare(indiceSimbolo(b.getKey()), indiceSimbolo(a.getKey()));
            }
        });

        int primaFrequenza   = voci.get(0).getValue();
        int secondaFrequenza = voci.size() > 1 ? voci.get(1).getValue() : 0;
        int primoIndice      = indiceSimbolo(voci.get(0).getKey());
        int secondoIndice    = voci.size() > 1 ? indiceSimbolo(voci.get(1).getKey()) : 0;

        if (primaFrequenza == 5)                           return new int[]{ 6, primoIndice, 0            };
        if (primaFrequenza == 4)                           return new int[]{ 5, primoIndice, 0            };
        if (primaFrequenza == 3 && secondaFrequenza == 2) return new int[]{ 4, primoIndice, secondoIndice };
        if (primaFrequenza == 3)                           return new int[]{ 3, primoIndice, 0            };
        if (primaFrequenza == 2 && secondaFrequenza == 2) return new int[]{ 2, primoIndice, secondoIndice };
        if (primaFrequenza == 2)                           return new int[]{ 1, primoIndice, 0            };
        return new int[]{ 0, primoIndice, 0 };
    }

    private int confronta(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i]) return Integer.compare(a[i], b[i]);
        return 0;
    }

    private int indiceSimbolo(String simbolo) {
        for (int i = 0; i < SIMBOLI.length; i++)
            if (SIMBOLI[i].equals(simbolo)) return i;
        return 0;
    }

    public void prossimoTurno() { stato = ATTESA; }

    public int     getStato()                  { return stato; }
    public String  getCartaGiocatore(int i)    { return manoGiocatore[i]; }
    public String  getCartaLuigi(int i)        { return manoLuigi[i]; }

    public String[] manoOrdinata(String[] mano) {
        final Map<String, Integer> frequenze = costruisciFrequenze(mano);
        List<String> ordinata = new ArrayList<>(Arrays.asList(mano));
        Collections.sort(ordinata, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                int differenza = Integer.compare(frequenze.get(b), frequenze.get(a));
                return differenza != 0 ? differenza : Integer.compare(indiceSimbolo(b), indiceSimbolo(a));
            }
        });
        return ordinata.toArray(new String[0]);
    }

    public String[] getManoOrdinataGiocatore() { return manoOrdinata(manoGiocatore); }
    public String[] getManoOrdinataLuigi()      { return manoOrdinata(manoLuigi); }
    public boolean  isScartata(int i)           { return daScartare[i]; }
    public int[]    getPunteggioGiocatore()     { return punteggioGiocatore; }
    public int[]    getPunteggioLuigi()         { return punteggioLuigi; }
    public int      getEsito()                  { return esito; }
    public int      getVittorie()               { return vittorie; }
    public int      getSconfitte()              { return sconfitte; }
    public int      getPareggi()                { return pareggi; }
    public String   nomeCombinazione(int[] p)   { return COMBINAZIONI[p[0]]; }
}
