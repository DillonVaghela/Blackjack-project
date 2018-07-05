package com.mygdx.game.Domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Domain.CardNumber;
import com.mygdx.game.Domain.Suit;


public class Card implements Comparable<Card> {

    private Suit suit;
    private CardNumber cardNumber;


    public Card(Suit suit, CardNumber cardNumber)
    {

        this.suit = suit;
        this.cardNumber = cardNumber;

    }


    public Texture getCard()
    {

            String filePath = this.suit.toString() + "_" + this.cardNumber.number + ".png";

        return new Texture(Gdx.files.internal(filePath) );

    }

    public int getNumber()
    {
        return cardNumber.number;
    }

    public Suit getSuit() { return suit;}

    public int compareTo(Card card) {

        int compareValue = card.cardNumber.number;


        return this.cardNumber.number - compareValue;



    }

    public String toString()
    {
        return cardNumber.number + " " + suit;
    }


}
