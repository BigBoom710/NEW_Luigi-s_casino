import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView extends JFrame {

    private static final String[] PERCORSI_IMMAGINI = {
        "carte/carta_5_copia.png", 
        "carte/carta_1_copia.png", 
        "carte/carta_2_copia.png", 
        "carte/carta_3_copia.png", 
        "carte/carta_4_copia.png", 
        "carte/carta_6_copia.png"  
    };

    private JLabel   punteggioLabel, bannerLabel;
    private JLabel   nomeManoGiocatore, nomeManoLuigi;
    private JLabel[] carteGiocatore    = new JLabel[GameModel.DIMENSIONE];
    private JLabel[] carteLuigi        = new JLabel[GameModel.DIMENSIONE];
    private JButton[] bottoniScarto    = new JButton[GameModel.DIMENSIONE];
    private JButton  bottoneAzione;

    public GameView() {
        super("Luigi's Picture Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        add(costruisciAlto(), BorderLayout.NORTH);
        
        JPanel areaGioco = new JPanel(new BorderLayout(0, 8));
        areaGioco.add(costruisciMani(), BorderLayout.CENTER);
        areaGioco.add(costruisciBasso(), BorderLayout.SOUTH);
        add(areaGioco, BorderLayout.CENTER);
        
        add(costruisciLegenda(), BorderLayout.EAST);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JPanel costruisciAlto() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 4));
        punteggioLabel = new JLabel("Vinte: 0  |  Perse: 0  |  Pari: 0", SwingConstants.CENTER);
        punteggioLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bannerLabel = new JLabel(" ", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        bannerLabel.setOpaque(true);
        p.add(punteggioLabel);
        p.add(bannerLabel);
        return p;
    }

    private JPanel costruisciMani() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 16));
        p.add(costruisciPannelloMano("Luigi", carteLuigi,  nomeManoLuigi  = new JLabel("Luigi", SwingConstants.CENTER)));
        p.add(costruisciPannelloMano("Tu",    carteGiocatore, nomeManoGiocatore = new JLabel("Tu",    SwingConstants.CENTER)));
        return p;
    }

    private JPanel costruisciPannelloMano(String titolo, JLabel[] carte, JLabel etichettaNome) {
        etichettaNome.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel riga = new JPanel(null);
        riga.setPreferredSize(new Dimension(85 * GameModel.DIMENSIONE + 6 * (GameModel.DIMENSIONE - 1), 120));
        for (int i = 0; i < GameModel.DIMENSIONE; i++) {
            carte[i] = creaCarta();
            carte[i].setBounds(i * (85 + 6), 0, 85, 120);
            riga.add(carte[i]);
        }
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.add(etichettaNome, BorderLayout.NORTH);
        p.add(riga,          BorderLayout.CENTER);
        return p;
    }

    private JLabel creaCarta() {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setPreferredSize(new Dimension(85, 120));
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return l;
    }

    private JPanel costruisciLegenda() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createTitledBorder("Gerarchia Carte"));
        p.setPreferredSize(new Dimension(140, 0));

        JLabel titolo = new JLabel("<html><div style='text-align: center;'>Dal più debole<br>al più forte</div></html>", SwingConstants.CENTER);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 11));
        p.add(titolo, BorderLayout.NORTH);

        JPanel lista = new JPanel(new GridLayout(6, 1, 0, 4));
        for (int i = 0; i < GameModel.SIMBOLI.length; i++) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
            item.setOpaque(false);
            
            JLabel numero = new JLabel((i + 1) + ".");
            numero.setFont(new Font("SansSerif", Font.BOLD, 12));
            numero.setForeground(Color.GRAY);
            item.add(numero);
            
            ImageIcon icona = caricaIcona(GameModel.SIMBOLI[i]);
            Image img = icona.getImage().getScaledInstance(28, 40, Image.SCALE_SMOOTH);
            JLabel lblIcona = new JLabel(new ImageIcon(img));
            lblIcona.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            item.add(lblIcona);
            
            JLabel lbl = new JLabel(GameModel.SIMBOLI[i]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
            item.add(lbl);
            
            lista.add(item);
        }
        p.add(lista, BorderLayout.CENTER);
        return p;
    }

    private JPanel costruisciBasso() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 6));

        JPanel riga = new JPanel(new GridLayout(1, GameModel.DIMENSIONE, 6, 0));
        for (int i = 0; i < GameModel.DIMENSIONE; i++) {
            bottoniScarto[i] = new JButton("Scarta");
            bottoniScarto[i].setEnabled(false);
            riga.add(bottoniScarto[i]);
        }
        p.add(riga);

        bottoneAzione = new JButton("Inizia");
        bottoneAzione.setFont(new Font("SansSerif", Font.BOLD, 13));
        p.add(bottoneAzione);

        return p;
    }

    public void aggiornaPunteggio(int vittorie, int sconfitte, int pareggi) {
        punteggioLabel.setText("Vinte: " + vittorie + "  |  Perse: " + sconfitte + "  |  Pari: " + pareggi);
    }

    public void mostraCartaLuigi(int i, String simbolo) {
        if (simbolo == null) {
            carteLuigi[i].setIcon(null);
            carteLuigi[i].setText("?");
            carteLuigi[i].setForeground(Color.WHITE);
            carteLuigi[i].setBackground(new Color(40, 40, 110));
        } else if (simbolo.isEmpty()) {
            carteLuigi[i].setIcon(null);
            carteLuigi[i].setText("");
            carteLuigi[i].setForeground(Color.BLACK);
            carteLuigi[i].setBackground(Color.LIGHT_GRAY);
        } else {
            carteLuigi[i].setText("");
            carteLuigi[i].setIcon(caricaIcona(simbolo));
            carteLuigi[i].setBackground(Color.WHITE);
        }
        carteLuigi[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    public void mostraCartaGiocatore(int i, String simbolo, boolean scartata) {
        if (simbolo == null) {
            carteGiocatore[i].setIcon(null);
            carteGiocatore[i].setText("");
            carteGiocatore[i].setBackground(Color.LIGHT_GRAY);
            carteGiocatore[i].setForeground(Color.BLACK);
            carteGiocatore[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
        } else {
            carteGiocatore[i].setText("");
            carteGiocatore[i].setIcon(caricaIcona(simbolo));
            carteGiocatore[i].setBackground(Color.WHITE);
            if (scartata) {
                carteGiocatore[i].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            } else {
                carteGiocatore[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            }
        }
        if (bottoniScarto[i].isEnabled())
            bottoniScarto[i].setText(scartata ? "Tieni" : "Scarta");
    }

    public void impostaScartoAbilitato(boolean abilitato) {
        for (int i = 0; i < GameModel.DIMENSIONE; i++) {
            bottoniScarto[i].setEnabled(abilitato);
            bottoniScarto[i].setText("Scarta");
        }
    }

    public void impostaEtichettaAzione(String testo) { bottoneAzione.setText(testo); }

    public void mostraBanner(String testo, Color primoPiano, Color sfondo) {
        bannerLabel.setText(testo);
        bannerLabel.setForeground(primoPiano);
        bannerLabel.setBackground(sfondo);
    }

    public void animaRiordino(int[] permLuigi, int[] permGiocatore, Runnable fine) {
        Timer t = new Timer(20, null);
        int passiTotali = 25;
        int[] step = {0};
        
        int[] startXLuigi = new int[GameModel.DIMENSIONE];
        int[] startXGiocatore = new int[GameModel.DIMENSIONE];
        
        for(int i=0; i<GameModel.DIMENSIONE; i++) {
            startXLuigi[i] = carteLuigi[i].getX();
            startXGiocatore[i] = carteGiocatore[i].getX();
        }

        t.addActionListener(e -> {
            step[0]++;
            float frazione = (float) step[0] / passiTotali;
            // Easing
            float ease = frazione * (2 - frazione);

            for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                int targetX = i * (85 + 6);
                
                int oldIndexLuigi = permLuigi[i];
                int curXLuigi = startXLuigi[oldIndexLuigi];
                carteLuigi[oldIndexLuigi].setLocation((int)(curXLuigi + (targetX - curXLuigi) * ease), carteLuigi[oldIndexLuigi].getY());
                
                int oldIndexGiocatore = permGiocatore[i];
                int curXGiocatore = startXGiocatore[oldIndexGiocatore];
                carteGiocatore[oldIndexGiocatore].setLocation((int)(curXGiocatore + (targetX - curXGiocatore) * ease), carteGiocatore[oldIndexGiocatore].getY());
            }
            
            if (step[0] >= passiTotali) {
                t.stop();
                
                // Riordina l'array di JLabel per rispecchiare la logica
                JLabel[] nuoveCarteLuigi = new JLabel[GameModel.DIMENSIONE];
                JLabel[] nuoveCarteGiocatore = new JLabel[GameModel.DIMENSIONE];
                for (int i = 0; i < GameModel.DIMENSIONE; i++) {
                    nuoveCarteLuigi[i] = carteLuigi[permLuigi[i]];
                    nuoveCarteGiocatore[i] = carteGiocatore[permGiocatore[i]];
                }
                carteLuigi = nuoveCarteLuigi;
                carteGiocatore = nuoveCarteGiocatore;
                
                if (fine != null) fine.run();
            }
        });
        t.start();
    }
    
    public void resettaPosizioniCarte() {
        for (int i = 0; i < GameModel.DIMENSIONE; i++) {
            carteLuigi[i].setBounds(i * (85 + 6), 0, 85, 120);
            carteGiocatore[i].setBounds(i * (85 + 6), 0, 85, 120);
        }
    }

    public void abilitaBottoneAzione(boolean abilitato) {
        bottoneAzione.setEnabled(abilitato);
    }

    public void pulisciBanner() {
        bannerLabel.setText(" ");
        bannerLabel.setBackground(getBackground());
    }

    public void impostaNomeManoGiocatore(String s) { nomeManoGiocatore.setText("Tu — " + s); }
    public void impostaNomeManoLuigi(String s)  { nomeManoLuigi.setText("Luigi — " + s); }
    public void reimpostaNomiMani()             { nomeManoGiocatore.setText("Tu"); nomeManoLuigi.setText("Luigi"); }

    public void aggiungiAscoltatoreAzione(ActionListener l)        { bottoneAzione.addActionListener(l); }
    public void aggiungiAscoltatoreScarto(int i, ActionListener l) { bottoniScarto[i].addActionListener(l); }

    private ImageIcon caricaIcona(String simbolo) {
        int indice = indiceSimbolo(simbolo);
        ImageIcon icona = new ImageIcon(PERCORSI_IMMAGINI[indice]);
        Image immagine = icona.getImage().getScaledInstance(75, 105, Image.SCALE_SMOOTH);
        return new ImageIcon(immagine);
    }

    private int indiceSimbolo(String nome) {
        for (int i = 0; i < GameModel.SIMBOLI.length; i++)
            if (GameModel.SIMBOLI[i].equals(nome)) return i;
        return 0;
    }
}
