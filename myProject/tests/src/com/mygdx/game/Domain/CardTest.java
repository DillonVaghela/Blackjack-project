package com.mygdx.game.Domain;

import org.junit.Test;

import static org.junit.Assert.*;
import de.tomgrill.gdxtesting.GdxTestRunner;

import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class CardTest {

    private final Card card = new Card(Suit.spade, CardNumber.Eight);

    @Test
    public void getNumber() {
        assertEquals(card.getNumber(),CardNumber.Eight.number);
    }

    @Test
    public void getSuit() {
        assertEquals(card.getSuit(), Suit.spade);
    }

    @Test
    public void compareTo() {
        assertEquals(card.compareTo(new Card(Suit.spade, CardNumber.Nine)),-1);
    }
}