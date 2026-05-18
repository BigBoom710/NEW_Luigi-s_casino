import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView extends JFrame {

    // Colori per simbolo (stesso ordine di GameModel.CHARACTERS)
    private static final Color[] CARD_BG = {
        new Color(255, 220,  50),  // Stella  - giallo oro
        new Color(220,  80,  80),  // Mario   - rosso
        new Color( 80, 180,  80),  // Luigi   - verde
        new Color(255, 140,  40),  // Fiore   - arancione
        new Color(170, 100, 210),  // Fungo   - viola
        new Color(140, 200, 255),  // Nuvola  - azzurro
    };
    private static final Color FACEDOWN = new Color(40, 40, 110);

    // ── Componenti ────────────────────────────────────────────────────────────

    private JLabel   scoreLabel, bannerLabel;
    private JLabel   playerHandName, luigiHandName;
    private JLabel[] playerCards    = new JLabel[GameModel.HAND_SIZE];
    private JLabel[] luigiCards     = new JLabel[GameModel.HAND_SIZE];
    private JButton[] scartaButtons = new JButton[GameModel.HAND_SIZE];
    private JButton  actionButton;

    // ── Costruzione UI ────────────────────────────────────────────────────────

    public GameView() {
        super("Luigi's Picture Poker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(buildTop(),    BorderLayout.NORTH);
        add(buildHands(),  BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JPanel buildTop() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 4));
        scoreLabel = new JLabel("Vinte: 0  |  Perse: 0  |  Pari: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        bannerLabel = new JLabel(" ", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        bannerLabel.setOpaque(true);
        p.add(scoreLabel);
        p.add(bannerLabel);
        return p;
    }

    private JPanel buildHands() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 16));
        p.add(buildHandPanel("Luigi", luigiCards,  luigiHandName  = new JLabel("Luigi", SwingConstants.CENTER)));
        p.add(buildHandPanel("Tu",    playerCards, playerHandName = new JLabel("Tu",    SwingConstants.CENTER)));
        return p;
    }

    private JPanel buildHandPanel(String title, JLabel[] cards, JLabel nameLabel) {
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel row = new JPanel(new GridLayout(1, GameModel.HAND_SIZE, 6, 0));
        for (int i = 0; i < GameModel.HAND_SIZE; i++) {
            cards[i] = makeCard();
            row.add(cards[i]);
        }
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.add(nameLabel, BorderLayout.NORTH);
        p.add(row,       BorderLayout.CENTER);
        return p;
    }

    private JLabel makeCard() {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setPreferredSize(new Dimension(85, 90));
        l.setOpaque(true);
        l.setBackground(Color.LIGHT_GRAY);
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return l;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 6));

        JPanel row = new JPanel(new GridLayout(1, GameModel.HAND_SIZE, 6, 0));
        for (int i = 0; i < GameModel.HAND_SIZE; i++) {
            scartaButtons[i] = new JButton("Scarta");
            scartaButtons[i].setEnabled(false);
            row.add(scartaButtons[i]);
        }
        p.add(row);

        actionButton = new JButton("Inizia");
        actionButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        p.add(actionButton);

        return p;
    }

    // ── Aggiornamento (chiamato dal Controller) ───────────────────────────────

    public void updateScore(int w, int l, int d) {
        scoreLabel.setText("Vinte: " + w + "  |  Perse: " + l + "  |  Pari: " + d);
    }

    /** character=null → coperta, character="" → vuota */
    public void showLuigiCard(int i, String character) {
        if (character == null) {
            luigiCards[i].setText("?");
            luigiCards[i].setForeground(Color.WHITE);
            luigiCards[i].setBackground(FACEDOWN);
        } else if (character.isEmpty()) {
            luigiCards[i].setText("");
            luigiCards[i].setForeground(Color.BLACK);
            luigiCards[i].setBackground(Color.LIGHT_GRAY);
        } else {
            luigiCards[i].setText("<html><center>" + character + "</center></html>");
            luigiCards[i].setForeground(Color.BLACK);
            luigiCards[i].setBackground(CARD_BG[charIndex(character)]);
        }
        luigiCards[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    public void showPlayerCard(int i, String character, boolean discarded) {
        if (character == null) {
            playerCards[i].setText("");
            playerCards[i].setBackground(Color.LIGHT_GRAY);
            playerCards[i].setForeground(Color.BLACK);
            playerCards[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
        } else {
            playerCards[i].setText("<html><center>" + character + "</center></html>");
            playerCards[i].setBackground(discarded ? Color.DARK_GRAY : CARD_BG[charIndex(character)]);
            playerCards[i].setForeground(discarded ? Color.LIGHT_GRAY : Color.BLACK);
            playerCards[i].setBorder(BorderFactory.createLineBorder(
                discarded ? Color.RED : Color.DARK_GRAY, discarded ? 2 : 1));
        }
        if (scartaButtons[i].isEnabled())
            scartaButtons[i].setText(discarded ? "Tieni" : "Scarta");
    }

    public void setScartaEnabled(boolean enabled) {
        for (int i = 0; i < GameModel.HAND_SIZE; i++) {
            scartaButtons[i].setEnabled(enabled);
            scartaButtons[i].setText("Scarta");
        }
    }

    public void setActionLabel(String text) { actionButton.setText(text); }

    public void showBanner(String text, Color fg, Color bg) {
        bannerLabel.setText(text);
        bannerLabel.setForeground(fg);
        bannerLabel.setBackground(bg);
    }

    public void clearBanner() {
        bannerLabel.setText(" ");
        bannerLabel.setBackground(getBackground());
    }

    public void setPlayerHandName(String s) { playerHandName.setText("Tu — " + s); }
    public void setLuigiHandName(String s)  { luigiHandName.setText("Luigi — " + s); }
    public void resetHandNames()            { playerHandName.setText("Tu"); luigiHandName.setText("Luigi"); }

    // ── Listener ─────────────────────────────────────────────────────────────

    public void addActionListener(ActionListener l)        { actionButton.addActionListener(l); }
    public void addScartaListener(int i, ActionListener l) { scartaButtons[i].addActionListener(l); }

    // ── Helper ────────────────────────────────────────────────────────────────

    private int charIndex(String name) {
        for (int i = 0; i < GameModel.CHARACTERS.length; i++)
            if (GameModel.CHARACTERS[i].equals(name)) return i;
        return 0;
    }
}
