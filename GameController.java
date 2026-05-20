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
                for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                    vista.mostraCartaGiocatore(i, null, false);
                    vista.mostraCartaLuigi(i, "");
                }
                vista.impostaScartoAbilitato(false);
                vista.impostaEtichettaAzione("Inizia");
                vista.pulisciBanner();
                vista.reimpostaNomiMani();
                break;

            case GameModel.TURNO_GIOCATORE:
                for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                    vista.mostraCartaGiocatore(i, modello.getCartaGiocatore(i), modello.isScartata(i));
                    vista.mostraCartaLuigi(i, null);
                }
                vista.impostaScartoAbilitato(true);
                vista.impostaEtichettaAzione("Pesca");
                vista.pulisciBanner();
                vista.reimpostaNomiMani();
                break;

            case GameModel.RISULTATO:
                String[] manoOrdinataGiocatore = modello.getManoOrdinataGiocatore();
                String[] manoOrdinataLuigi     = modello.getManoOrdinataLuigi();
                for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                    vista.mostraCartaGiocatore(i, manoOrdinataGiocatore[i], false);
                    vista.mostraCartaLuigi(i, manoOrdinataLuigi[i]);
                }
                vista.impostaScartoAbilitato(false);
                vista.impostaEtichettaAzione("Ancora");
                vista.impostaNomeManoGiocatore(modello.nomeCombinazione(modello.getPunteggioGiocatore()));
                vista.impostaNomeManoLuigi(modello.nomeCombinazione(modello.getPunteggioLuigi()));

                int esito = modello.getEsito();
                if (esito > 0)
                    vista.mostraBanner("HAI VINTO!", new Color(0, 100, 0), new Color(200, 255, 200));
                else if (esito < 0)
                    vista.mostraBanner("HAI PERSO!", new Color(130, 0, 0), new Color(255, 200, 200));
                else
                    vista.mostraBanner("PAREGGIO", new Color(80, 60, 0), new Color(255, 240, 180));
                break;
        }

        vista.aggiornaPunteggio(modello.getVittorie(), modello.getSconfitte(), modello.getPareggi());
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
