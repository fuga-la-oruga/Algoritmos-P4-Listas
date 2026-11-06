package dev.reveles.controller;

import dev.reveles.model.Card;

public class Move
{
    public enum Location
    {
        TABLE,
        RESERVE,
        FOUNDATION
    }

    private Card card;
    private Location source;
    private Location destination;
    private int sourceIndex;
    private int destinationIndex;

    public Move
    (
        Card card,
        Location source,
        int sourceIndex,
        Location destination,
        int destinationIndex
    )
    {
        this.card = card;
        this.source = source;
        this.sourceIndex = sourceIndex;
        this.destination = destination;
        this.destinationIndex = destinationIndex;
    }

    public Card getCard() {return card;}
    public Location getSource() {return source;}
    public Location getDestination() {return destination;}
    public int getSourceIndex() {return sourceIndex;}
    public int getDestinationIndex() {return destinationIndex;}
}