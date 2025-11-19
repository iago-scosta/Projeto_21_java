package model;

public class Card {
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }

    private final String rank;
    private final Suit suit;

    public Card(String rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getImagePath() {
        return "cards/" + rank + "_" + suit.name() + ".png";
    }

    public String getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        if ("A".equals(rank)) return 11;
        if ("J".equals(rank) || "Q".equals(rank) || "K".equals(rank)) return 10;
        return Integer.parseInt(rank);
    }

    public String imageFileName() {
        return rank + "_" + suit.name() + ".png";
    }
}

