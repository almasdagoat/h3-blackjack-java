import java.util.ArrayList;

public class Player {
    private ArrayList<Card> hand;
    private double balance;

    public Player(double balance) {
        this.hand = new ArrayList<>();
        this.balance = balance;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }

    public double getBalance() {
        return balance;
    }

    public void adjustBalance(double amount) {
        balance += amount;
    }

    public int getHandValue() {
        int value = 0;
        int aces = 0;

        for (Card card : hand) {
            if (card.getValue() == 1) {
                aces++;
            } else {
                value += card.getValue();
            }
        }

        for (int i = 0; i < aces; i++) {
            if (value + 11 <= 21) {
                value += 11;
            } else {
                value += 1;
            }
        }
        return value;
    }
}