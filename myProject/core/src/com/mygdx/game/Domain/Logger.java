package com.mygdx.game.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Logger implements Serializable {

    private Result trueHit;
    private Result falseHit;

    public Logger()
    {
        this.trueHit = new Result();
        this.falseHit = new Result();
    }

    @Override
    public String toString()
    {
        return trueHit.toString() + " : " + falseHit.toString();
    }

    public Result getTrueHit()
    {
        return trueHit;
    }

    public Result getFalseHit()
    {
        return falseHit;
    }

    public void addMove(boolean status)
    {
        if (status)
        {
            trueHit.count ++;
        }
        else
        {
            falseHit.count ++;
        }
    }

    public void addResult(boolean status, boolean win)
    {
        if (status)
        {
            if (win)
            {
                trueHit.countWins++;
            }
            else
            {
                trueHit.countLoses++;
            }
        }
        else
        {
            if (win)
            {
                falseHit.countWins++;
            }
            else
            {
                falseHit.countLoses++;
            }
        }
    }


}
