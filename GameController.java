import javax.swing.SwingUtilities;
import java.awt.Color;

public class GameController {

    private final GameModel modello;
    private final GameView  vista;

    public GameController(GameModel modello, GameView vista) {
        this.modello = modello;
        this.vista   = vista;
        registraAscoltatori();
        aggiorna();
    }

    private void registraAscoltatori() {
        vista.aggiungiAscoltatoreAzione(e -> gestisciAzione());

        for (int i = 0; i < GameModel.DIMENSIONE; i++) {
            final int indice = i;
            vista.aggiungiAscoltatoreScarto(i, e -> {
                modello.toggleScarto(indice);
                vista.mostraCartaGiocatore(indice, modello.getCartaGiocatore(indice), modello.isScartata(indice));
            });
        }
    }

    private void gestisciAzione() {
        switch (modello.getStato()) {
            case GameModel.ATTESA:        modello.distribuisci();           break;
            case GameModel.TURNO_GIOCATORE: modello.pescaERisolvi();        break;
            case GameModel.RISULTATO:     modello.prossimoTurno();          break;
        }
        aggiorna();
    }

    private void aggiorna() {
        switch (modello.getStato()) {

            case GameModel.ATTESA:
                vista.resettaPosizioniCarte();
                for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                    vista.mostraCartaGiocatore(i, null, false);
                    vista.mostraCartaLuigi(i, "");
                }
                vista.impostaScartoAbilitato(false);
                vista.impostaEtichettaAzione("Inizia");
                vista.pulisciBanner();
                vista.reimpostaNomiMani();
                vista.aggiornaPunteggio(modello.getVittorie(), modello.getSconfitte(), modello.getPareggi());
                break;

            case GameModel.TURNO_GIOCATORE:
                vista.impostaEtichettaAzione("Pesca");
                vista.pulisciBanner();
                vista.reimpostaNomiMani();
                vista.aggiornaPunteggio(modello.getVittorie(), modello.getSconfitte(), modello.getPareggi());
                vista.impostaScartoAbilitato(false);
                vista.abilitaBottoneAzione(false);
                
                // Animazione distribuzione
                javax.swing.Timer t = new javax.swing.Timer(150, null);
                int[] stepDistribuzione = {0};
                t.addActionListener(e -> {
                    int i = stepDistribuzione[0];
                    if (i < GameModel.DIMENSIONE) {
                        vista.mostraCartaGiocatore(i, modello.getCartaGiocatore(i), false);
                        vista.mostraCartaLuigi(i, null); // dorso
                        stepDistribuzione[0]++;
                    } else {
                        ((javax.swing.Timer)e.getSource()).stop();
                        vista.impostaScartoAbilitato(true);
                        vista.abilitaBottoneAzione(true);
                    }
                });
                t.start();
                break;

            case GameModel.RISULTATO:
                vista.impostaScartoAbilitato(false);
                vista.impostaEtichettaAzione("Ancora");
                vista.abilitaBottoneAzione(false);
                
                int[] permLuigi = modello.getPermutazioneLuigi();
                int[] permGioc = modello.getPermutazioneGiocatore();
                
                // Animazione rivelazione e scarto
                javax.swing.Timer t2 = new javax.swing.Timer(250, null);
                int[] stepRivelazione = {0};
                t2.addActionListener(e -> {
                    int i = stepRivelazione[0];
                    if (i < GameModel.DIMENSIONE) {
                        vista.mostraCartaGiocatore(i, modello.getCartaGiocatore(i), false);
                        vista.mostraCartaLuigi(i, modello.getCartaLuigi(i));
                        stepRivelazione[0]++;
                    } else {
                        ((javax.swing.Timer)e.getSource()).stop();
                        
                        // Dopo aver rivelato tutto, piccola pausa poi anima riordino
                        javax.swing.Timer t3 = new javax.swing.Timer(500, ev -> {
                            vista.animaRiordino(permLuigi, permGioc, () -> {
                                vista.impostaNomeManoGiocatore(modello.nomeCombinazione(modello.getPunteggioGiocatore()));
                                vista.impostaNomeManoLuigi(modello.nomeCombinazione(modello.getPunteggioLuigi()));

                                int esito = modello.getEsito();
                                if (esito > 0)
                                    vista.mostraBanner("HAI VINTO!", new Color(0, 100, 0), new Color(200, 255, 200));
                                else if (esito < 0)
                                    vista.mostraBanner("HAI PERSO!", new Color(130, 0, 0), new Color(255, 200, 200));
                                else
                                    vista.mostraBanner("PAREGGIO", new Color(80, 60, 0), new Color(255, 240, 180));
                                
                                vista.aggiornaPunteggio(modello.getVittorie(), modello.getSconfitte(), modello.getPareggi());
                                vista.abilitaBottoneAzione(true);
                            });
                        });
                        t3.setRepeats(false);
                        t3.start();
                    }
                });
                t2.start();
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel modello = new GameModel();
            GameView  vista   = new GameView();
            new GameController(modello, vista);
            vista.setVisible(true);
        });
    }
}
