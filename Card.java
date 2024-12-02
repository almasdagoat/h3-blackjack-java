import javax.swing.ImageIcon;

public class Card {
    private String suit;
    private String rank;
    private int value;
    private ImageIcon image;

    public Card(String suit, String rank, int value, ImageIcon image) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
        this.image = image;
    }

    public int getValue() { 
        return value; 
    }
    
    public ImageIcon getImage() { 
        return image; 
    }
}