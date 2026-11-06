package dev.reveles.model;

public class Card implements Comparable<Card>
{
    private int value;
    private Suit suit;

    // ------------------------------

    public Card(int value, Suit suit)
    {
        this.value = value;
        this.suit = suit;
    }

    // ------------------------------

    public int getValue() {return value;}
    public Suit getSuit() {return suit;}

    public String getDisplayValue()
    {
        switch(value)
        {
            case 1: return "A";
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            default: return String.valueOf(value);
        }
    }

    @Override
    public int compareTo(Card other)
    {
        if(value != other.value)
        {
            return Integer.compare(value, other.value);
        }

        return suit.compareTo(other.suit);
    }

    @Override
    public String toString()
    {
        return getDisplayValue() + suit.getSymbol();
    }
}