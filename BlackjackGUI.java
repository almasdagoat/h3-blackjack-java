import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BlackjackGUI extends JFrame {
    private Deck deck;
    private Player player;
    private Player dealer;
    private double currentBet;
    private ImageIcon cardBack;

    private JPanel dealerPanel;
    private JPanel playerPanel;
    private JLabel balanceLabel;
    private JLabel betLabel;
    private JLabel messageLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private JTextField betField;

    public BlackjackGUI() {
        super("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        // Create panels
        dealerPanel = new JPanel();
        playerPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        JPanel infoPanel = new JPanel();

        // Set backgrounds
        dealerPanel.setBackground(new Color(0, 100, 0));
        playerPanel.setBackground(new Color(0, 100, 0));
        controlPanel.setBackground(new Color(0, 100, 0));
        infoPanel.setBackground(new Color(0, 100, 0));

        // Create components
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        newGameButton = new JButton("New Game");
        betField = new JTextField(10);
        balanceLabel = new JLabel("Balance: $1000.00");
        betLabel = new JLabel("Current Bet: $0.00");
        messageLabel = new JLabel("Place your bet to start");

        // Set colors
        balanceLabel.setForeground(Color.WHITE);
        betLabel.setForeground(Color.WHITE);
        messageLabel.setForeground(Color.WHITE);

        // Add components to panels
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        controlPanel.add(newGameButton);
        
        // Create and add bet label properly
        JLabel betTextLabel = new JLabel("Bet: $");
        betTextLabel.setForeground(Color.WHITE);
        controlPanel.add(betTextLabel);
        controlPanel.add(betField);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(balanceLabel);
        infoPanel.add(betLabel);
        infoPanel.add(messageLabel);

        // Add panels to frame
        add(dealerPanel, BorderLayout.NORTH);
        add(playerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.EAST);

        // Load card back
        try {
            Image img = ImageIO.read(new File("cards/BACK.png"));
            cardBack = new ImageIcon(img.getScaledInstance(80, 116, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            cardBack = new ImageIcon();
        }

        // Initialize game
        deck = new Deck();
        player = new Player(1000.0);
        dealer = new Player(0);

        // Add button actions
        hitButton.addActionListener(e -> hit());
        standButton.addActionListener(e -> stand());
        newGameButton.addActionListener(e -> startNewGame());
        betField.addActionListener(e -> placeBet());

        // Initial button states
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeBet() {
        try {
            double bet = Double.parseDouble(betField.getText());
            if (bet > 0 && bet <= player.getBalance()) {
                currentBet = bet;
                betField.setEnabled(false);
                betLabel.setText("Current Bet: $" + bet);
                
                // Deal cards
                player.clearHand();
                dealer.clearHand();
                player.addCard(deck.drawCard());
                dealer.addCard(deck.drawCard());
                player.addCard(deck.drawCard());
                dealer.addCard(deck.drawCard());

                hitButton.setEnabled(true);
                standButton.setEnabled(true);
                updateDisplay();
            }
        } catch (Exception e) {
            betField.setText("");
        }
    }

    private void hit() {
        player.addCard(deck.drawCard());
        updateDisplay();
        if (player.getHandValue() > 21) {
            endRound();
        }
    }

    private void stand() {
        while (dealer.getHandValue() < 17) {
            dealer.addCard(deck.drawCard());
        }
        endRound();
    }

    private void endRound() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        int playerValue = player.getHandValue();
        int dealerValue = dealer.getHandValue();

        if (playerValue > 21) {
            messageLabel.setText("Bust! You lose!");
            player.adjustBalance(-currentBet);
        } else if (dealerValue > 21) {
            messageLabel.setText("Dealer busts! You win!");
            player.adjustBalance(currentBet);
        } else if (playerValue > dealerValue) {
            messageLabel.setText("You win!");
            player.adjustBalance(currentBet);
        } else if (dealerValue > playerValue) {
            messageLabel.setText("Dealer wins!");
            player.adjustBalance(-currentBet);
        } else {
            messageLabel.setText("Push!");
        }

        balanceLabel.setText("Balance: $" + player.getBalance());
        updateDisplay();
    }

    private void startNewGame() {
        dealer.clearHand();
        player.clearHand();
        dealerPanel.removeAll();
        playerPanel.removeAll();
        betField.setEnabled(true);
        betField.setText("");
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        messageLabel.setText("Place your bet to start");
        betLabel.setText("Current Bet: $0.00");
        updateDisplay();
    }

    private void updateDisplay() {
        dealerPanel.removeAll();
        playerPanel.removeAll();

        // Show dealer's cards
        ArrayList<Card> dealerCards = dealer.getHand();
        if (!dealerCards.isEmpty()) {
            if (hitButton.isEnabled()) {
                dealerPanel.add(new JLabel(cardBack));
                for (int i = 1; i < dealerCards.size(); i++) {
                    dealerPanel.add(new JLabel(dealerCards.get(i).getImage()));
                }
                dealerPanel.add(new JLabel("Value: ?"));
            } else {
                for (Card card : dealerCards) {
                    dealerPanel.add(new JLabel(card.getImage()));
                }
                dealerPanel.add(new JLabel("Value: " + dealer.getHandValue()));
            }
        }

        // Show player's cards
        ArrayList<Card> playerCards = player.getHand();
        for (Card card : playerCards) {
            playerPanel.add(new JLabel(card.getImage()));
        }
        if (!playerCards.isEmpty()) {
            playerPanel.add(new JLabel("Value: " + player.getHandValue()));
        }

        dealerPanel.revalidate();
        dealerPanel.repaint();
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackjackGUI game = new BlackjackGUI();
            game.setVisible(true);
        });
    }
}
