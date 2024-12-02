import java.awt.*;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"H", "D", "C", "S"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        
        for (String suit : suits) {
            for (String rank : ranks) {
                try {
                    String imagePath = "cards/" + rank + "-" + suit + ".png";
                    Image img = ImageIO.read(new File(imagePath));
                    Image smallerImg = img.getScaledInstance(80, 116, Image.SCALE_SMOOTH);
                    ImageIcon cardImage = new ImageIcon(smallerImg);
                    
                    int value;
                    if (rank.equals("A")) {
                        value = 1;
                    } else if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
                        value = 10;
                    } else {
                        value = Integer.parseInt(rank);
                    }
                    
                    cards.add(new Card(suit, rank, value, cardImage));
                } catch (Exception e) { }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }
}