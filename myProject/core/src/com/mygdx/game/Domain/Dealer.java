package com.mygdx.game.Domain;

import java.io.Serializable;

public class Dealer extends Player implements Serializable {
    public Dealer()
    {
        super();
    }

    public boolean computeHit(Logger log)
    {
        Result trueHit = log.getTrueHit();
        Result falseHit = log.getFalseHit();
        int hitWin = 0;
        int hitLose = 0;
        int standWin = 0;
        int standLose = 0;
        try {
            hitWin = (trueHit.count/trueHit.count+falseHit.count)*(trueHit.countWins/trueHit.count);
            hitLose = (trueHit.count/trueHit.count+falseHit.count)*(trueHit.countLoses/trueHit.count);
            standWin = (falseHit.count/trueHit.count+falseHit.count)*(falseHit.countWins/falseHit.count);
            standLose = (falseHit.count/trueHit.count+falseHit.count)*(falseHit.countLoses/falseHit.count);
        }
        catch (Exception e)
        {
            return false;
        }
        if (hitWin > hitLose)
        {
            if (hitWin > standWin)
            {
                return true;
            }
            else
            {
                if (standWin > standLose)
                {
                    return false;
                }
                if (standLose > hitLose)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else {
            if (standWin > standLose) {
                return true;
            } else {
                if (standLose > hitLose) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
