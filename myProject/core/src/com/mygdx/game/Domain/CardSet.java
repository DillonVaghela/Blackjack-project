package com.mygdx.game.Domain;




import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardSet
{
    private Card[][] set;
    ArrayList<String> cardsPicked;

    public CardSet()
    {
        cardsPicked = new ArrayList<String>();
        int suitLength = Suit.values().length;
        int cardLength = CardNumber.values().length;
        set = new Card[suitLength][cardLength];
        for (Suit suit : Suit.values())
        {
            for (CardNumber cardNumber : CardNumber.values())
            {
                set[suit.index][cardNumber.index] = new Card(suit, cardNumber);
            }
        }
    }


    public Card getRandomCard()
    {
        Card card = null;
        int suit = 0;
        int number = 0;
        while (card == null)
        {
            suit = randomSuit();
            number = randomNumber();
            if (!(cardsPicked.contains(suit + " " + number)))
            {
                card = set[suit][number];
            }
        }
        cardsPicked.add(suit + " " + number);
        return card;
    }



    private int randomSuit()
    {
        Random rand = new Random();
        return rand.nextInt(Suit.values().length );
    }

    private int randomNumber()
    {
        Random rand = new Random();
        return rand.nextInt(CardNumber.values().length);
    }


    public Double getBustProbability(int difference)
    {
        double count = 0;
        if (difference >=10)
        {
            return 0.0/52;
        }
        for (Suit suit : Suit.values())
        {

            for (CardNumber cardNumber : CardNumber.values())
            {
                int temp = cardNumber.number > 10 ? 10: cardNumber.number;
                if (temp > difference)
                {
                    break;
                }
                if (!(cardsPicked.contains(suit + " " + cardNumber.number)))
                {
                    count++;
                }

            }
        }
        return count/52;
    }

}
