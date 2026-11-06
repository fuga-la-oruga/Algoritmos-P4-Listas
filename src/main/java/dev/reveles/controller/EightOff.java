package dev.reveles.controller;

import dev.reveles.model.Card;
import dev.reveles.model.Deck;
import dev.reveles.model.SimpleList;

public class EightOff
{
    private Deck deck;
    private SimpleList<Card>[] table;
    private Card[] reserve;
    private SimpleList<Card>[] foundations;
    private SimpleList<Move> history;

    private boolean gameOver;
    private boolean won;

    // ------------------------------

    public EightOff()
    {
        table = (SimpleList<Card>[]) new SimpleList[8];
        foundations = (SimpleList<Card>[]) new SimpleList[4];

        reserve = new Card[8];
        history = new SimpleList<Move>();

        newGame();
    }

    // ------------------------------

    public void newGame()
    {
        deck = new Deck();
        deck.shuffle();

        history = new SimpleList<Move>();

        for(int i = 0; i < 8; i ++)
        {
            table[i] = new SimpleList<Card>();
        }

        for(int i = 0; i < 4; i ++)
        {
            foundations[i] = new SimpleList<Card>();
        }

        reserve = new Card[8];

        for(int i = 0; i < 8; i ++)
        {
            for(int j = 0; j < 6; j ++)
            {
                table[i].addLast(deck.drawCard());
            }
        }

        for(int i = 0; i < 4; i ++)
        {
            reserve[i] = deck.drawCard();
        }

        gameOver = false;
        won = false;
    }

    public Card getTableCard(int col, int i)
    {
        if(col < 0 || col >= 8)
        {
            return null;
        }

        return table[col].get(i);
    }

    public Card getTableTop(int col)
    {
        if(col < 0 || col >= 8)
        {
            return null;
        }

        return table[col].getLast();
    }

    public int getTableSize(int col)
    {
        if(col < 0 || col >= 8)
        {
            return 0;
        }

        return table[col].size();
    }

    public Card getReserveCard(int i)
    {
        if(i < 0 || i >= 8)
        {
            return null;
        }

        return reserve[i];
    }

    public Card getFoundationTop(int i)
    {
        if(i < 0 || i >= 4)
        {
            return null;
        }

        return foundations[i].getLast();
    }

    public int getFoundationSize(int i)
    {
        if(i < 0 || i >= 4)
        {
            return 0;
        }

        return foundations[i].size();
    }

    public int getAvailableReserveSlot()
    {
        for(int i = 0; i < 8; i ++)
        {
            if(reserve[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean getGameOver()
    {
        return gameOver;
    }

    public boolean hasWon()
    {
        return won;
    }

    private Card getSourceCard(Move.Location src, int i)
    {
        if(src == Move.Location.TABLE)
        {
            if(i < 0 || i >= 8)
            {
                return null;
            }

            return table[i].getLast();
        }

        if(src == Move.Location.RESERVE)
        {
            if(i < 0 || i >= 8)
            {
                return null;
            }

            return reserve[i];
        }

        return null;
    }

    private boolean canPlaceOnTable(Card card, int col)
    {
        if(card == null || col < 0 || col >= 8)
        {
            return false;
        }

        Card top = table[col].getLast();

        if(top == null)
        {
            return true;
        }

        return top.getSuit() == card.getSuit() && top.getValue() == card.getValue() + 1;
    }

    private boolean canPlaceOnFoundation(Card card, int i)
    {
        if(card == null || i < 0 || i >= 4)
        {
            return false;
        }

        Card top = foundations[i].getLast();

        if(top == null)
        {
            return card.getValue() == 1;
        }

        return
            card.getSuit() == top.getSuit() &&
            card.getValue() == top.getValue() + 1;
    }

    private SimpleList<Card> getMovableSequence(int col, int start)
    {
        if(col < 0 || col >= 8)
        {
            return null;
        }

        if(start < 0 || start >= table[col].size())
        {
            return null;
        }

        SimpleList<Card> sequence = new SimpleList<Card>();

        Card previous = null;

        for(int i = start; i < table[col].size(); i ++)
        {
            Card current = table[col].get(i);

            if(previous != null)
            {
                boolean sameSuit = previous.getSuit() == current.getSuit();
                boolean descending = previous.getValue() == current.getValue() + 1;

                if(!sameSuit || !descending)
                {
                    return null;
                }
            }

            sequence.addLast(current);
            previous = current;
        }

        return sequence;
    }

    public boolean isSelectable(int col, int cardIndex)
    {
        return getMovableSequence(col, cardIndex) != null;
    }

    public boolean isValidMove
    (
        Move.Location src,
        int srcI,
        Move.Location dest,
        int destI
    )
    {
        if(gameOver || src == null || dest == null)
        {
            return false;
        }

        if(src == dest && srcI == destI)
        {
            return false;
        }

        Card card = getSourceCard(src, srcI);

        if(card == null)
        {
            return false;
        }

        if(dest == Move.Location.TABLE)
        {
            if(src == Move.Location.TABLE && srcI == destI)
            {
                return false;
            }

            return canPlaceOnTable(card, destI);
        }

        if(dest == Move.Location.RESERVE)
        {
            if(src == Move.Location.RESERVE)
            {
                return false;
            }

            if(destI < 0 || destI >= 8)
            {
                return false;
            }

            return reserve[destI] == null;
        }

        if(dest == Move.Location.FOUNDATION)
        {
            return canPlaceOnFoundation(card, destI);
        }

        return false;
    }

    public boolean isValidSequenceMove(int srcI, int cardIndex, int destI)
    {
        if(gameOver || srcI < 0 || srcI >= 8 || destI < 0 || destI >= 8 || srcI == destI)
        {
            return false;
        }

        SimpleList<Card> sequence = getMovableSequence(srcI, cardIndex);

        if(sequence == null)
        {
            return false;
        }

        return canPlaceOnTable(sequence.getFirst(), destI);
    }

    public boolean moveCard
    (
        Move.Location src,
        int srcI,
        Move.Location dest,
        int destI
    )
    {
        if(!isValidMove(src, srcI, dest, destI))
        {
            return false;
        }

        Card card = getSourceCard(src, srcI);

        SimpleList<Card> cards = new SimpleList<Card>();
        cards.addLast(card);

        if(src == Move.Location.TABLE)
        {
            table[srcI].removeLast();
        }
        else if(src == Move.Location.RESERVE)
        {
            reserve[srcI] = null;
        }

        placeCards(cards, dest, destI);

        Move move = new Move(cards, src, srcI, dest, destI);

        history.addFirst(move);

        updateGameState();

        return true;
    }

    public boolean moveSequence(int srcI, int cardIndex, int destI)
    {
        if(!isValidSequenceMove(srcI, cardIndex, destI))
        {
            return false;
        }

        SimpleList<Card> cards = getMovableSequence(srcI, cardIndex);

        for(int i = 0; i < cards.size(); i ++)
        {
            table[srcI].removeLast();
        }

        placeCards(cards, Move.Location.TABLE, destI);

        Move move = new Move
        (
            cards,
            Move.Location.TABLE,
            srcI,
            Move.Location.TABLE,
            destI
        );

        history.addFirst(move);

        updateGameState();

        return true;
    }

    private void placeCards
    (
        SimpleList<Card> cards,
        Move.Location dest,
        int destI
    )
    {
        if(dest == Move.Location.TABLE)
        {
            for(int i = 0; i < cards.size(); i ++)
            {
                table[destI].addLast(cards.get(i));
            }
        }
        else if(dest == Move.Location.RESERVE)
        {
            reserve[destI] = cards.getFirst();
        }
        else if(dest == Move.Location.FOUNDATION)
        {
            foundations[destI].addLast(cards.getFirst());
        }
    }

    public boolean undoMove()
    {
        if(history.isEmpty())
        {
            return false;
        }

        Move move = history.removeFirst();

        SimpleList<Card> cards = move.getCards();

        if(move.getDestination() == Move.Location.TABLE)
        {
            for(int i = 0; i < cards.size(); i ++)
            {
                table[move.getDestinationIndex()].removeLast();
            }
        }
        else if(move.getDestination() == Move.Location.RESERVE)
        {
            reserve[move.getDestinationIndex()] = null;
        }
        else if(move.getDestination() == Move.Location.FOUNDATION)
        {
            foundations[move.getDestinationIndex()].removeLast();
        }

        if(move.getSource() == Move.Location.TABLE)
        {
            for(int i = 0; i < cards.size(); i ++)
            {
                table[move.getSourceIndex()].addLast(cards.get(i));
            }
        }
        else if(move.getSource() == Move.Location.RESERVE)
        {
            reserve[move.getSourceIndex()] = cards.getFirst();
        }

        updateGameState();

        return true;
    }

    public SimpleList<Move> getHints()
    {
        SimpleList<Move> hints = new SimpleList<Move>();

        if(gameOver)
        {
            return hints;
        }

        // TABLE -> TABLE
        for(int i = 0; i < 8; i ++)
        {
            for(int j = 0; j < table[i].size(); j ++)
            {
                SimpleList<Card> cards = getMovableSequence(i, j);

                if(cards == null)
                {
                    continue;
                }

                for(int k = 0; k < 8; k ++)
                {
                    if(i != k && canPlaceOnTable(cards.getFirst(), k))
                    {
                        hints.addLast(new Move(
                                cards,
                                Move.Location.TABLE,
                                i,
                                Move.Location.TABLE,
                                k
                        ));
                    }
                }
            }
        }

        // TABLE -> FOUNDATION
        for(int i = 0; i < 8; i ++)
        {
            Card card = table[i].getLast();

            if(card != null)
            {
                for(int j = 0; j < 4; j ++)
                {
                    if(canPlaceOnFoundation(card, j))
                    {
                        SimpleList<Card> cards = new SimpleList<Card>();
                        cards.addLast(card);

                        hints.addLast(new Move(
                                cards,
                                Move.Location.TABLE,
                                i,
                                Move.Location.FOUNDATION,
                                j
                        ));
                    }
                }
            }
        }

        // RESERVE -> TABLE
        for(int i = 0; i < 8; i ++)
        {
            if(reserve[i] != null)
            {
                for(int j = 0; j < 8; j ++)
                {
                    if(canPlaceOnTable(reserve[i], j))
                    {
                        SimpleList<Card> cards = new SimpleList<Card>();
                        cards.addLast(reserve[i]);

                        hints.addLast(new Move(
                                cards,
                                Move.Location.RESERVE,
                                i,
                                Move.Location.TABLE,
                                j
                        ));
                    }
                }
            }
        }

        // RESERVE -> FOUNDATION
        for(int i = 0; i < 8; i ++) {
            if (reserve[i] != null) {
                for (int j = 0; j < 4; j++) {
                    if (canPlaceOnFoundation(reserve[i], j)) {
                        SimpleList<Card> cards = new SimpleList<Card>();
                        cards.addLast(reserve[i]);

                        hints.addLast(new Move(
                                cards,
                                Move.Location.RESERVE,
                                i,
                                Move.Location.FOUNDATION,
                                j
                        ));
                    }
                }
            }
        }

        return hints;
    }

    public Move getHint()
    {
        SimpleList<Move> hints = getHints();

        if(hints.isEmpty())
        {
            return null;
        }

        return hints.getFirst();
    }

    public boolean hasAvailableMoves()
    {
        return !getHints().isEmpty();
    }

    private void updateGameState()
    {
        won = true;

        for(int i = 0; i < 4; i ++)
        {
            if(foundations[i].size() != 13)
            {
                won = false;
                break;
            }
        }

        if(won)
        {
            gameOver = true;
            return;
        }

        gameOver = false;
        gameOver = !hasAvailableMoves();
    }

    public boolean canUndo()
    {
        return !history.isEmpty();
    }
}