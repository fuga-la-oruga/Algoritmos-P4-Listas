package dev.reveles.model;

public enum Suit
{
    CLUBS("♣", "black"),
    DIAMONDS("♦", "red"),
    HEARTS("♥", "red"),
    SPADES("♠", "black");

    private final String symbol;
    private final String color;

    Suit(String symbol, String color)
    {
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol() {return symbol;}
    public String getColor() {return color;}
}