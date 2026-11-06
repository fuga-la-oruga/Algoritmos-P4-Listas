package dev.reveles.controller;

import dev.reveles.model.Card;
import dev.reveles.model.SimpleList;

public class Move
{
    public enum Location
    {
        TABLE,
        RESERVE,
        FOUNDATION
    }

    private SimpleList<Card> cards;
    private Location source;
    private Location destination;
    private int sourceIndex;
    private int destinationIndex;

    // ------------------------------

    public Move(SimpleList<Card> cards, Location source, int sourceIndex,
                Location destination, int destinationIndex)
    {
        this.cards = cards;
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.destination = destination;
        this.destinationIndex = destinationIndex;
    }

    // ------------------------------

    public Card getCard(){return cards.getFirst();}
    public SimpleList<Card> getCards(){return cards;}
    public int getCardCount(){return cards.size();}
    public Location getSource(){return source;}
    public Location getDestination(){return destination;}
    public int getSourceIndex(){return sourceIndex;}
    public int getDestinationIndex() {return destinationIndex;}
}