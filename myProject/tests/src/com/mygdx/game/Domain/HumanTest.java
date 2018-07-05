package com.mygdx.game.Domain;

import com.badlogic.gdx.Gdx;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class HumanTest {
    Human human = new Human("Dillon");


    @Test
    public void testPlayerChip() {
        human.setPlayerChip(40, (Gdx.graphics.getHeight()*1/4)+50, 130,(Gdx.graphics.getHeight()*1/4)+60,
                220,(Gdx.graphics.getHeight()*1/4)+70,130+(85/2)
                ,(Gdx.graphics.getHeight()*1/4)-90,130+(85/2) + 90,(Gdx.graphics.getHeight()*1/4)-100
                ,Gdx.graphics.getWidth()*1/4-220, (Gdx.graphics.getWidth()*1/4)-170, (Gdx.graphics.getWidth()*1/4)-120, (Gdx.graphics.getWidth()*1/4)-70, (Gdx.graphics.getWidth()*1/4)-20);
        assertEquals(human.getPlayerChip().toString(), new PlayerChip(40, (Gdx.graphics.getHeight()*1/4)+50, 130,(Gdx.graphics.getHeight()*1/4)+60,
                220,(Gdx.graphics.getHeight()*1/4)+70,130+(85/2)
                ,(Gdx.graphics.getHeight()*1/4)-90,130+(85/2) + 90,(Gdx.graphics.getHeight()*1/4)-100
                ,Gdx.graphics.getWidth()*1/4-220, (Gdx.graphics.getWidth()*1/4)-170, (Gdx.graphics.getWidth()*1/4)-120, (Gdx.graphics.getWidth()*1/4)-70, (Gdx.graphics.getWidth()*1/4)-20).toString());
    }

    @Test
    public void getName() {
        assertEquals(human.getName(), "Dillon");
    }

    @Test
    public void resetAndAddCard() {
        human.addCard(new Card(Suit.spade, CardNumber.Eight));
        assertEquals(human.getTotal(), 8);
        human.reset();
        assertEquals(human.getTotal(), 0);
    }

    @Test
    public void getBetMoney() {
        assertEquals(human.getBetMoney(), 1000);
    }

    @Test
    public void removeBetMoney() {
        assertEquals(human.getBetMoney(), 1000);
        human.removeBetMoney(50);
        assertEquals(human.getBetMoney(), 950);
    }

    @Test
    public void addBetMoney() {
        assertEquals(human.getBetMoney(), 1000);
        human.addBetMoney(50);
        assertEquals(human.getBetMoney(), 1050);
    }

    @Test
    public void addAce()
    {
        human.add11Ace();
        assertEquals(human.getTotal(), 10);
    }


}