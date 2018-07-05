package com.mygdx.game.Domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerChip implements Serializable {

    public int oneChipX;
    public int oneChipY;
    public int fiveChipX;
    public int fiveChipY;
    public int tenChipX;
    public int tenChipY;
    public int twentyFiveChipX;
    public int twentyFiveChipY;
    public int hundredChipX;
    public int hundredChipY;
    public int betHeightOne;
    public int betHeightFive;
    public int betHeightTen;
    public int betHeightTwentyFive;
    public int betHeightHundred;
    public int betHundredPosX;
    public int betTwentyFivePosX;
    public int betTenPosX;
    public int betFivePosX;
    public int betOnePosX;
    public int bettingMoney;
    public int perfectPairMoney;
    public int flushMoney;
    public ArrayList<ArrayList<Image>> perfectPairArray ;
    public ArrayList<ArrayList<Image>> flushArray ;
    public ArrayList<ArrayList<Image>> insuranceArray ;
    private boolean ace;

    public PlayerChip(int oneChipX, int oneChipY, int fiveChipX, int fiveChipY, int tenChipX, int tenChipY
            , int twentyFiveChipX, int twentyFiveChipY, int hundredChipX, int hundredChipY, int betHundredPosX, int betTwentyFivePosX, int betTenPosX
    , int betFivePosX, int betOnePosX)
    {
        this.oneChipX =oneChipX;
        this.oneChipY = oneChipY;
        this.fiveChipX = fiveChipX;
        this.fiveChipY = fiveChipY;
        this.tenChipX = tenChipX;
        this.tenChipY = tenChipY;
        this.twentyFiveChipX = twentyFiveChipX;
        this.twentyFiveChipY = twentyFiveChipY;
        this.hundredChipX = hundredChipX;
        this.hundredChipY = hundredChipY;
        betHeightOne = (Gdx.graphics.getHeight()*1/4)+210 ;
        betHeightFive =(Gdx.graphics.getHeight()*1/4)+220 ;
        betHeightTen = (Gdx.graphics.getHeight()*1/4)+230 ;
        betHeightTwentyFive = (Gdx.graphics.getHeight()*1/4)+240 ;
        betHeightHundred =  (Gdx.graphics.getHeight()*1/4)+250;
        bettingMoney = 0;
        perfectPairMoney = 0;
        flushMoney = 0;
        this.betHundredPosX = betHundredPosX;
        this.betTwentyFivePosX = betTwentyFivePosX;
        this.betTenPosX = betTenPosX;
        this.betFivePosX = betFivePosX;
        this.betOnePosX = betOnePosX;
        ace = false;
        resetArrayLists();

    }

    public void reset()
    {
        betHeightOne =  (Gdx.graphics.getHeight()*1/4)+200;
        betHeightFive = (Gdx.graphics.getHeight()*1/4)+200;
        betHeightTen = (Gdx.graphics.getHeight()*1/4)+200;
        betHeightTwentyFive = (Gdx.graphics.getHeight()*1/4)+200;
        betHeightHundred = (Gdx.graphics.getHeight()*1/4)+200;
        bettingMoney = 0;
        perfectPairMoney = 0;
        flushMoney = 0;
        ace = false;
        resetArrayLists();
    }

    private void resetArrayLists()
    {
        flushArray = new ArrayList<ArrayList<Image>>();
        perfectPairArray = new ArrayList<ArrayList<Image>>();
        insuranceArray = new ArrayList<ArrayList<Image>>();
        flushArray.add(new ArrayList<Image>());
        flushArray.add(new ArrayList<Image>());
        flushArray.add(new ArrayList<Image>());
        flushArray.add(new ArrayList<Image>());
        flushArray.add(new ArrayList<Image>());
        perfectPairArray.add(new ArrayList<Image>());
        perfectPairArray.add(new ArrayList<Image>());
        perfectPairArray.add(new ArrayList<Image>());
        perfectPairArray.add(new ArrayList<Image>());
        perfectPairArray.add(new ArrayList<Image>());
        insuranceArray.add(new ArrayList<Image>());
        insuranceArray.add(new ArrayList<Image>());
        insuranceArray.add(new ArrayList<Image>());
        insuranceArray.add(new ArrayList<Image>());
        insuranceArray.add(new ArrayList<Image>());
    }

    public void setAce()
    {
        ace = true;
    }

    public boolean hasAce()
    {
        return ace;
    }

    @Override
    public String toString()
    {
        return betHeightOne + betHeightFive + betHeightTen + betHeightTwentyFive + betHeightHundred + bettingMoney + betFivePosX + betHundredPosX +
                betOnePosX + betTenPosX + betTwentyFivePosX + perfectPairMoney + flushMoney + flushArray.size() + perfectPairArray.size() + "" + ace;
    }
}
