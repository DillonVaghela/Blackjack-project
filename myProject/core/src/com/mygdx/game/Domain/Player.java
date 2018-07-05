package com.mygdx.game.Domain;

import com.mygdx.game.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable
{

    private int cardTotal;


    public Player()
    {
        cardTotal = 0;

    }



    public void addCard(Card card)
    {
        if (card.getNumber()> 10)
        {
            cardTotal -= card.getNumber() - 10;
        }
        cardTotal += card.getNumber();
    }

    public int getTotal()
    {
        return cardTotal;
    }



    public void add11Ace()
    {
        cardTotal += 10;
    }

    public void reset()
    {
        cardTotal = 0;
    }



}
