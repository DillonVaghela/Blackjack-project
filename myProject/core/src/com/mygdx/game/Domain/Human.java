package com.mygdx.game.Domain;

import java.io.Serializable;

public class Human extends Player implements Serializable {
    private int betMoney;
    private PlayerChip playerChip;
    private String name;

    public Human(String name)
    {
        super();
        this.name = name;
        this.betMoney = 1000;

    }

    public void setPlayerChip(int oneChipX, int oneChipY, int fiveChipX, int fiveChipY, int tenChipX, int tenChipY, int twentyFiveChipX
            , int twentyFiveChipY, int hundredChipX, int hundredChipY,  int betHundredPosX, int betTwentyFivePosX, int betTenPosX
            , int betFivePosX, int betOnePosX) {
        playerChip = new PlayerChip(oneChipX, oneChipY, fiveChipX, fiveChipY, tenChipX, tenChipY, twentyFiveChipX
                , twentyFiveChipY, hundredChipX, hundredChipY, betHundredPosX, betTwentyFivePosX, betTenPosX
                , betFivePosX,betOnePosX);
    }

    public String getName()
    {
        return name;
    }

    public void reset()
    {
        super.reset();
        if (playerChip != null) {
            playerChip.reset();
        }
    }

    public int getBetMoney()
    {
        return betMoney;
    }

    public void removeBetMoney(int amount)
    {
        betMoney -= amount;
    }

    public void addBetMoney(int amount)
    {
        betMoney += amount;
    }

    public PlayerChip getPlayerChip()
    {
        return playerChip;
    }
}
