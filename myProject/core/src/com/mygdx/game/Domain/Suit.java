package com.mygdx.game.Domain;

import com.badlogic.gdx.graphics.Color;

public enum Suit {
    spade(0, Color.BLACK), heart(1, Color.RED), diamond(2, Color.RED), club(3, Color.BLACK);

    public final int index;
    public final Color color;

    Suit(int index, Color color)
    {
        this.index = index;
        this.color = color;
    }
}
