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

    // ------------------------------

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
        if(col < 0 || col >= 8)
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
        if(i < 0 || i >= 4)
        {
            return false;
        }

        if(card.getSuit().ordinal() != i)
        {
            return false;
        }

        Card top = foundations[i].getLast();

        if(top == null)
        {
            return card.getValue() == 1;
        }

        return card.getValue() == top.getValue() + 1;
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

    // ------------------------------

    public boolean moveCard(Move.Location src, int srcI, Move.Location dest, int destI)
    {
        if(!isValidMove(src, srcI, dest, destI))
        {
            return false;
        }

        Card card = getSourceCard(src, srcI);

        if(src == Move.Location.TABLE)
        {
            table[srcI].removeLast();
        }
        else if(src == Move.Location.RESERVE)
        {
            reserve[srcI] = null;
        }

        if(dest == Move.Location.TABLE)
        {
            table[destI].addLast(card);
        }
        else if(dest == Move.Location.RESERVE)
        {
            reserve[destI] = card;
        }
        else if(dest == Move.Location.FOUNDATION)
        {
            foundations[destI].addLast(card);
        }

        Move move = new Move(card, src, srcI,
                dest, destI);

        history.addFirst(move);

        updateGameState();

        return true;
    }

    // ------------------------------

    public boolean undoMove()
    {
        if(history.isEmpty())
        {
            return false;
        }

        Move move = history.removeFirst();

        Card card = move.getCard();

        if(move.getDestination() == Move.Location.TABLE)
        {
            table[move.getDestinationIndex()].removeLast();
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
            table[move.getSourceIndex()].addLast(card);
        }
        else if(move.getSource() == Move.Location.RESERVE)
        {
            reserve[move.getSourceIndex()] = card;
        }

        updateGameState();

        return true;
    }

    // ------------------------------

    public Move getHint()
    {
        if(gameOver)
        {
            return null;
        }

        for(int i = 0; i < 8; i ++)
        {
            Card card = table[i].getLast();

            if(card != null)
            {
                Move move = findMove(Move.Location.TABLE, i);

                if(move != null)
                {
                    return move;
                }
            }
        }

        for(int i = 0; i < 8; i ++)
        {
            if(reserve[i] != null)
            {
                Move move = findMove(Move.Location.RESERVE, i);

                if(move != null)
                {
                    return move;
                }
            }
        }

        return null;
    }

    private Move findMove(Move.Location src, int srcI)
    {
        Card card = getSourceCard(src, srcI);

        for(int i = 0; i < 8; i ++)
        {
            if(src != Move.Location.TABLE || srcI != i)
            {
                if(isValidMove(src, srcI, Move.Location.TABLE, i))
                {
                    return new Move(card, src, srcI, Move.Location.TABLE, i);
                }
            }
        }

        for(int i = 0; i < 4; i ++)
        {
            if(isValidMove(src, srcI, Move.Location.FOUNDATION, i))
            {
                return new Move(card, src, srcI, Move.Location.FOUNDATION, i);
            }
        }

        if(src == Move.Location.TABLE)
        {
            int slot = getAvailableReserveSlot();

            if(slot != -1)
            {
                return new Move(card, src, srcI, Move.Location.RESERVE, slot);
            }
        }

        return null;
    }

    // ------------------------------

    public boolean hasAvailableMoves()
    {
        return getHint() != null;
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
}