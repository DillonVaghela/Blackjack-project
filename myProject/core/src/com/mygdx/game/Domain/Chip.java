package com.mygdx.game.Domain;

import com.badlogic.gdx.graphics.Texture;

public enum Chip {
    oneDollarChip(1, "poker chips/$1.png"), fiveDollarChip(5, "poker chips/$5.png"), tenDollarChip(10, "poker chips/$10.png"), twentyFiveDollarChip(25, "poker chips/$25.png"), hundredDollarChip(100, "poker chips/$100.png");

    public final int value;
    public final Texture image;

    Chip(int value, String file)
    {
        this.value = value;
        this.image = new Texture(file) ;
    }

}
