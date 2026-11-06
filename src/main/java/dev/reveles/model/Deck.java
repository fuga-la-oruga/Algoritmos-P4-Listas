package dev.reveles.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck
{
    private CircularDoubleList<Card> cards;

    // ------------------------------

    public Deck()
    {
        cards = new CircularDoubleList<Card>();
        createDeck();
    }

    // ------------------------------

    private void createDeck()
    {
        for(Suit suit : Suit.values())
        {
            for(int value = 1; value <= 13; value++)
            {
                cards.addLast(new Card(value, suit));
            }
        }
    }

    public void shuffle()
    {
        ArrayList<Card> temp = new ArrayList<Card>();

        while(!cards.isEmpty())
        {
            temp.add(cards.removeFirst());
        }

        Collections.shuffle(temp);

        for(Card card : temp)
        {
            cards.addLast(card);
        }
    }

    public Card drawCard()
    {
        return cards.removeFirst();
    }

    public boolean isEmpty()
    {
        return cards.isEmpty();
    }

    public int size()
    {
        return cards.size();
    }
}