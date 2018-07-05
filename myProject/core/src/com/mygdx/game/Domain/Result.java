package com.mygdx.game.Domain;

import java.io.Serializable;

public class Result implements Serializable {

    public int count;
    public int countWins;
    public int countLoses;

    public Result()
    {
        count = 0;
        countWins = 0;
        countLoses = 0;
    }

    @Override
    public String toString()
    {
        return count + " . " + countWins + " . " + countLoses;
    }
}
