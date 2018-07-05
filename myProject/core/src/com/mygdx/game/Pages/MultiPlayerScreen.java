package com.mygdx.game.Pages;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;
import com.mygdx.game.BlackJack;
import com.mygdx.game.Domain.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.mygdx.game.Domain.MediaLoader.*;

public class MultiPlayerScreen  implements Screen
{

    private static Stage stage;
    private Game game;
    private Batch batch;
    private Texture background, cardDeckImage;
    private Dialog Conformation, noBet, saveDialog, actionButtonsPrompt, bettingDialog;
    private CardSet cardSet;
    private Dealer dealer;
    private Human playerOne, playerTwo;
    private Image cardDeck, dealFirstCard, firstDealerCard, cardBack, secondDealerCard;
    private boolean start, firstCards, playerTwoHasDouble, playerOneHasDouble, playerInsurance, player2Insurance, checkFor21, playerStand, placedBets, gameEnd, player2Turn, playerOneOut, playerTwoOut, firstRound, playerOneBetReady, playerTwoBetReady, dealerHadAce, tutorialMode, flushActive, perfectPairActive;
    private static boolean terminateApp;
    private int playerPosition, dealerPosition, player2Position, previousPlayerOneBet, previousPlayerTwoBet;
    private TextButton hitButton, standButton, placeBetsButton, doubleButton, allInButtonPlayerOne, allInButtonPlayerTwo, repeatButtonPlayerOne, repeatButtonPlayerTwo, tutorialButton, promptsButton, PerfectPairButton, flushButton;
    private Label playerOneChips, playerNameLabel, player2NameLabel, playerBetTotal, playerBetTotal2, playerTwoChips, playerOneCardTotal, playerTwoCardTotal, playerOneInsuranceLabel, playerTwoInsuranceLabel, playerOnePerfectPairBetLabel, playerTwoPerfectPairBetLabel, playerOneFlushBetLabel, playerTwoFlushBetLabel;
    private ArrayList<Image> betChipsHundred, betChipsTwentyFive, betChipsTen, betChipsFive, betChipsOne;
    private Array tempMoves;
    private static MediaLoader mediaLoader = MediaLoader.getInstance();
    private Logger[][][] previousScenarios;
    private static Array menuScreenGameData = MenuScreen.getGameData();
    private static FileChooser fileChooser = (FileChooser) menuScreenGameData.get(0);
    private static Screen screen;
    private Preferences prefs = Gdx.app.getPreferences("Multiplayer Preferences");
    private Hashtable<String, String> hashTable = new Hashtable<String, String>();
    private Json json = new Json();
    private Dialog endOfRoundDialog;

    public MultiPlayerScreen(Game aGame) {

        screen = this;

        this.game = aGame;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        firstCards = false;
        placedBets = false;
        playerStand = false;
        player2Turn = false;
        playerOneOut = false;
        playerTwoOut = false;
        terminateApp = false;
        firstCards = true;
        playerTwoHasDouble = false;
        playerOneHasDouble = false;
        playerOneBetReady = false;
        playerTwoBetReady = false;
        player2Insurance = false;
        betChipsHundred = new ArrayList<Image>();
        betChipsTwentyFive = new ArrayList<Image>();
        betChipsTen = new ArrayList<Image>();
        betChipsFive = new ArrayList<Image>();
        betChipsOne = new ArrayList<Image>();
        tempMoves = new Array();
        previousPlayerOneBet = 0;
        previousPlayerTwoBet = 0;
        firstRound = true;

        previousScenarios = new Logger[22][22][22];

        background = new Texture("tableBackgroundMultiplayer.png");
        cardDeckImage = new Texture("back.jpg");
        cardSet = new CardSet();
        dealer = new Dealer();
        playerOne = new Human(NameScreen.getNameOne());
        playerTwo = new Human(NameScreen.getNameTwo());

        cardDeck = newCardImage(cardDeckImage);
        cardDeck.setPosition(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160);
        cardDeck.setName("card deck");

        Image oneDChip = new Image(Chip.oneDollarChip.image);
        oneDChip.setName("chip");
        Image fiveDChip = new Image(Chip.fiveDollarChip.image);
        fiveDChip.setName("chip");
        final Image tenDChip = new Image(Chip.tenDollarChip.image);
        tenDChip.setName("chip");
        Image twentyFiveDChip = new Image(Chip.twentyFiveDollarChip.image);
        twentyFiveDChip.setName("chip");
        Image hundredDChip = new Image(Chip.hundredDollarChip.image);
        hundredDChip.setName("chip");

        stage.addActor(mediaLoader.getBackgroundMusic());
        stage.addActor(mediaLoader.getSoundEffects());

        playerOneInsuranceLabel = new Label("", BlackJack.mySkin);
        playerOneInsuranceLabel.setPosition(285,Gdx.graphics.getHeight()-18);
        playerOneInsuranceLabel.setSize(5,5);
        stage.addActor(playerOneInsuranceLabel);

        playerTwoInsuranceLabel = new Label("", BlackJack.mySkin);
        playerTwoInsuranceLabel.setPosition(565,Gdx.graphics.getHeight()-18);
        playerTwoInsuranceLabel.setSize(5,5);
        stage.addActor(playerTwoInsuranceLabel);

        playerOnePerfectPairBetLabel = new Label("", BlackJack.mySkin);
        playerOnePerfectPairBetLabel.setPosition(285,Gdx.graphics.getHeight()-112);
        playerOnePerfectPairBetLabel.setSize(5,5);
        stage.addActor(playerOnePerfectPairBetLabel);

        playerTwoPerfectPairBetLabel = new Label("", BlackJack.mySkin);
        playerTwoPerfectPairBetLabel.setPosition(565,Gdx.graphics.getHeight()-112);
        playerTwoPerfectPairBetLabel.setSize(5,5);
        stage.addActor(playerTwoPerfectPairBetLabel);

        playerOneFlushBetLabel = new Label("", BlackJack.mySkin);
        playerOneFlushBetLabel.setPosition(615,Gdx.graphics.getHeight()-18);
        playerOneFlushBetLabel.setSize(5,5);
        stage.addActor(playerOneFlushBetLabel);

        playerTwoFlushBetLabel = new Label("", BlackJack.mySkin);
        playerTwoFlushBetLabel.setPosition(895,Gdx.graphics.getHeight()-18);
        playerTwoFlushBetLabel.setSize(5,5);
        stage.addActor(playerTwoFlushBetLabel);

        PerfectPairButton = new TextButton("Perfect Pair", BlackJack.mySkin, "toggle");
        PerfectPairButton.setWidth(Gdx.graphics.getWidth()/8);
        PerfectPairButton.setPosition(Gdx.graphics.getWidth()/2 - ((Gdx.graphics.getWidth()/8)/2), (Gdx.graphics.getHeight()/2- 31/2)+50);
        stage.addActor(PerfectPairButton);
        PerfectPairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (perfectPairActive)
                {
                    perfectPairActive = false;
                }
                else
                {
                    perfectPairActive = true;
                    if (flushActive)
                    {
                        flushActive = false;
                        flushButton.setChecked(false);
                    }
                }
            }
        });

        flushButton = new TextButton("Flush/ 21+3", BlackJack.mySkin, "toggle");
        flushButton.setWidth(Gdx.graphics.getWidth()/8);
        flushButton.setPosition(Gdx.graphics.getWidth()/2 - ((Gdx.graphics.getWidth()/8)/2), (Gdx.graphics.getHeight()/2- 31/2)+100);
        stage.addActor(flushButton);
        flushButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (flushActive)
                {
                    flushActive = false;
                }
                else
                {
                    flushActive = true;
                    if (perfectPairActive)
                    {
                        perfectPairActive = false;
                        PerfectPairButton.setChecked(false);
                    }
                }
            }
        });

        actionButtonsPrompt = new Dialog("How to play", BlackJack.mySkin);
        final Label text = new Label(
                "Double button will double your bet\n" +
                        "Hit button gets a new card\n" +
                        "Stand means you are happy with your cards and the dealer finishes their turn ", BlackJack.mySkin, "title");
        actionButtonsPrompt.text(text);
        actionButtonsPrompt.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        actionButtonsPrompt.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actionButtonsPrompt.hide();

            };
        });
        actionButtonsPrompt.setMovable(false);

        bettingDialog = new Dialog("How to play", BlackJack.mySkin);
        final Label description = new Label(
                "Place a bet by selecting a chip.\n" +
                        "All in allows you to place all your betting money on this bet.\n" +
                        "Repeat bet will appear once you have entered a bet on a previous round \n" +
                        "You can control the music and sound effects in the bottom right corner", BlackJack.mySkin, "title");
        bettingDialog.text(description);
        bettingDialog.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        bettingDialog.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bettingDialog.hide();

            };
        });
        bettingDialog.setMovable(false);

        tutorialButton = new TextButton("Tutorial Mode", BlackJack.mySkin, "toggle");
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tutorialMode)
                {
                    tutorialMode = false;
                }
                else
                {
                    tutorialMode = true;
                }
            }
        });
        tutorialButton.setPosition(Gdx.graphics.getWidth()-160 + (mediaLoader.getBackgroundMusic().getWidth() - tutorialButton.getWidth()), 75);
        if (NameScreen.getIsTutorialMode())
        {
            tutorialButton.setChecked(true);
            tutorialMode = true;

        }
        else
        {
            tutorialButton.setChecked(false);
        }
        stage.addActor(tutorialButton);

        final Dialog additionalBettingPrompt = new Dialog("Prompt", BlackJack.mySkin);
        final Label promptBettingText = new Label("Perfect pair \n" +
                " If your 2 cards are same number \n coloured match returns 15:1 \n mixed match returns 5:1 \n21+3 \n" +
                " Straight Flush (e.g. 7d-8d-9d) wins 40:1 \n Three of a Kind (e.g. 3d-3h-3s) wins 30:1 \n Straight (e.g. 9c-10d-Jh) wins 10:1 \n Flush (e.g. 2h-6h-10h) 5:1 \n click to hide", BlackJack.mySkin, "title-plain");
        additionalBettingPrompt.text(promptBettingText);
        additionalBettingPrompt.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        additionalBettingPrompt.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                additionalBettingPrompt.hide();
            };
        });

        final Dialog additionalHittingPrompt = new Dialog("Prompt", BlackJack.mySkin);
        final Label promptHittingText = new Label(" Recommendations\n" +
                " stand if your card total is 16 or more \n do no bet large amounts when low on chips, build your chips up with smaller bets  ", BlackJack.mySkin, "title-plain");
        additionalHittingPrompt.text(promptHittingText);
        additionalHittingPrompt.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        additionalHittingPrompt.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                additionalHittingPrompt.hide();
            };
        });

        promptsButton = new TextButton("Prompts", BlackJack.mySkin);
        promptsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (stage.getActors().contains(placeBetsButton, true))
                {
                    additionalBettingPrompt.show(stage);
                }
                if (stage.getActors().contains(hitButton, true))
                {
                    additionalHittingPrompt.show(stage);
                }
            }
        });
        promptsButton.setPosition(Gdx.graphics.getWidth()-160+ (mediaLoader.getBackgroundMusic().getWidth() - promptsButton.getWidth()), 110);
        promptsButton.setChecked(false);
        stage.addActor(promptsButton);

        playerNameLabel = new Label(NameScreen.getNameOne(),BlackJack.mySkin, "title");
        playerNameLabel.setFontScale(1);
        playerNameLabel.setPosition((Gdx.graphics.getWidth()*1/2)-120,(Gdx.graphics.getHeight()*1/16));
        stage.addActor(playerNameLabel);
        player2NameLabel = new Label(NameScreen.getNameTwo(),BlackJack.mySkin, "title");
        player2NameLabel.setFontScale(1);
        player2NameLabel.setPosition((Gdx.graphics.getWidth()*1/2)+50,(Gdx.graphics.getHeight()*1/16));
        stage.addActor(player2NameLabel);

        oneDChip.setPosition(Gdx.graphics.getWidth()*1/128, Gdx.graphics.getHeight()-85);
        fiveDChip.setPosition((Gdx.graphics.getWidth()*1/128)+90, Gdx.graphics.getHeight()-85);
        tenDChip.setPosition(Gdx.graphics.getWidth()*1/128,Gdx.graphics.getHeight()-175);
        twentyFiveDChip.setPosition((Gdx.graphics.getWidth()*1/128)+90,Gdx.graphics.getHeight()-175);
        hundredDChip.setPosition(Gdx.graphics.getWidth()*1/128 + 90 + 90,Gdx.graphics.getHeight()-85);
        oneDChip.setSize(85,85);
        fiveDChip.setSize(85,85);
        tenDChip.setSize(85,85);
        twentyFiveDChip.setSize(85,85);
        hundredDChip.setSize(85,85);
        stage.addActor(oneDChip);
        stage.addActor(fiveDChip);
        stage.addActor(tenDChip);
        stage.addActor(twentyFiveDChip);
        stage.addActor(hundredDChip);

        fileChooser.clearListeners();
        fileChooser.setListener(new SingleFileChooserListener() {
            @Override
            protected void selected(FileHandle fileHandle) {
                fileChooser.fadeOut(1f);
                String filePath = fileHandle.file().getAbsolutePath();
                saveGameToFile(filePath);
            }
        });


        start = false;
        stage.addActor(mediaLoader.getBackgroundMusic());
        stage.addActor(mediaLoader.getSoundEffects());

        TextButton saveButton = new TextButton("Save",BlackJack.mySkin);
        saveButton.setWidth(Gdx.graphics.getWidth()/12);
        saveButton.setPosition(20,90);
        saveButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                saveDialog.show(stage);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(saveButton);

        hitButton = new TextButton("Hit",BlackJack.mySkin);
        hitButton.setWidth(Gdx.graphics.getWidth()/16);
        hitButton.setPosition((Gdx.graphics.getWidth()/2- hitButton.getWidth()/2)-60,(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2));
        hitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerOneOut && playerTwoOut)
                {
                    endRound();

                }
                if (!player2Turn)
                {
                    if (playerOneHasDouble)
                    {
                        playerOneOut  = true;
                    }
                    hitButton.remove();
                    standButton.remove();
                    doubleButton.remove();
                    newPlayerCard(playerOne);

                    if (!playerTwoOut)
                    {
                        player2Turn = true;
                        player2NameLabel.setColor(Color.RED);
                        playerNameLabel.setColor(Color.WHITE);
                        addButtons(1.7f);
                    }
                    else
                    {
                        boolean newDealerCardB = checkIfDealerNeedsNewCard();
                        if (newDealerCardB) {
                            addScenario(true);
                            addButtons(newDealerCard(1.7f));

                        } else {
                            addScenario(false);
                            addButtons(1.7f);
                        }
                    }
                    if (playerOne.getTotal() >=21)
                    {
                        playerOneOut = true;
                    }
                }
                else {
                    if (playerTwoHasDouble)
                    {
                        playerTwoOut  = true;
                    }
                    hitButton.remove();
                    standButton.remove();
                    doubleButton.remove();
                    if (firstRound)
                    {
                        firstRound = false;
                    }
                    newPlayerCard(playerTwo);
                    fixChips(null);
                    boolean newDealerCardB = checkIfDealerNeedsNewCard();
                    if (newDealerCardB) {
                        addScenario(true);
                        addButtons(newDealerCard(1.7f));

                    } else {
                        addScenario(false);
                        addButtons(1.7f);
                    }
                    if (!playerOneOut)
                    {
                        player2Turn = false;
                        player2NameLabel.setColor(Color.WHITE);
                        playerNameLabel.setColor(Color.RED );
                    }
                    if (playerTwo.getTotal() >=21)
                    {
                        playerTwoOut = true;
                    }
                }
            };
        });
        hitButton.addAction(Actions.sequence(Actions.scaleTo(0,0)));

        placeBetsButton = new TextButton("Place Bets",BlackJack.mySkin);
        placeBetsButton.setWidth(Gdx.graphics.getWidth()/8);
        placeBetsButton.setPosition((Gdx.graphics.getWidth()/2-placeBetsButton.getWidth()/2),(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2));
        placeBetsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerOne.getPlayerChip().bettingMoney == 0 || playerTwo.getPlayerChip().bettingMoney == 0)
                {
                    errorMessage("Place a bet");
                    return;
                }
                betsPlaced();
            };
        });




        standButton = new TextButton("Stand",BlackJack.mySkin);
        standButton.setWidth(Gdx.graphics.getWidth()/16);
        standButton.setPosition((Gdx.graphics.getWidth()/2- standButton.getWidth()/2)+75,(Gdx.graphics.getHeight()/2- standButton.getHeight()/2));
        standButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (firstCards && !player2Turn && !stage.getActors().contains(doubleButton, true))
                {
                    stage.addActor(doubleButton);
                }
                firstCards = false;
                if (player2Turn)
                {
                    player2Turn = false;
                    playerTwoOut = true;
                    playerNameLabel.setColor(Color.RED);
                    player2NameLabel.setColor(Color.WHITE);
                }
                else
                {
                    player2Turn = true;
                    playerOneOut = true;
                    playerNameLabel.setColor(Color.WHITE);
                    player2NameLabel.setColor(Color.RED);
                }
                if (dealerHadAce)
                {
                    checkToAddAce(dealer);
                }
                if (playerOneOut && playerTwoOut)
                {
                    endRound();
                }
            };
        });
        standButton.addAction(Actions.sequence(Actions.scaleTo(0,0)));

        float doubleButtonPositionX = (hitButton.getX() + (standButton.getX()+standButton.getWidth()))/2 - (standButton.getWidth()/2);
        doubleButton = new TextButton("Double",BlackJack.mySkin);
        doubleButton.setWidth(Gdx.graphics.getWidth()/16);
        doubleButton.setPosition(doubleButtonPositionX,(Gdx.graphics.getHeight()/2 - doubleButton.getHeight()/2));
        doubleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Human player;
                if (player2Turn)
                {
                    player = playerTwo;
                }
                else
                {
                    player = playerOne;
                }
                if (player.getBetMoney() < player.getPlayerChip().bettingMoney)
                {
                    errorMessage("not enough funds");
                    return;
                }
                if (player2Turn)
                {
                    playerTwoHasDouble = true;
                }
                else
                {
                    playerOneHasDouble = true;
                }
                doubleChips(player);
                player.getPlayerChip().bettingMoney += (player.getPlayerChip().bettingMoney);
                player.removeBetMoney(player.getPlayerChip().bettingMoney);
                displayBetTotal();
                doubleButton.remove();

            };
        });
        doubleButton.addAction(Actions.sequence(Actions.scaleTo(0,0)));



        final Dialog help = new Dialog("How to play", BlackJack.mySkin);
        final Label heading = new Label(" Reach a final score higher than the dealer without exceeding 21 \n \n" +
                "In this version of blackjack the dealer can draw a card after your go and can only draw a single card after you stand unless its the first round \n \n" +
                "Insurance places half your original bet, if the dealer has an ace " +
                "and you have insurance then you get your bet money back if you dont have insurance \n " +
                "the dealer  takes your money \n" +
                "If the dealer does not have an ace and you take out insurance you lose your money or if you don't take out insurance then the game continues as normal " +
                " \n insurance pays 2 to 1 \n \n This game " +
                "pays out 3 to 2 \n When receiving an ace the game automatically determines the value\n \n You can control the music and sound effects in the bottom right corner \n " +
                "You can view this by clicking how to play in the bottom right corner\nClick to hide", BlackJack.mySkin);
        help.text(heading);
        help.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        help.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                help.hide();
                if (!start)
                {
                    start = true;
                    showChips();
                    displayChipTotal();
                    stage.addActor(placeBetsButton);
                    stage.addActor(allInButtonPlayerOne);
                    stage.addActor(allInButtonPlayerTwo);
                    if (tutorialMode) {
                        bettingDialog.show(stage);
                    }
                }
            };
        });
        help.setMovable(false);

        setPlayerChips();




        playerBetTotal = new Label("",BlackJack.mySkin);
        playerBetTotal.setFontScale(1);
        playerBetTotal.setPosition((Gdx.graphics.getWidth() * 1 / 4)+40,(Gdx.graphics.getHeight()*1/3)+140);

        playerBetTotal2 = new Label("",BlackJack.mySkin);
        playerBetTotal2.setFontScale(1);

        playerOneCardTotal = new Label("Card Total: "+ playerOne.getTotal() ,BlackJack.mySkin, "title");
        playerOneCardTotal.setPosition((Gdx.graphics.getWidth()*1/4)+90,(Gdx.graphics.getHeight()*1/8)+10);
        playerOneCardTotal.setSize(30, 10);

        playerTwoCardTotal = new Label("Card Total: "+playerTwo.getTotal() ,BlackJack.mySkin, "title");
        playerTwoCardTotal.setPosition((Gdx.graphics.getWidth()*3/4)-160,(Gdx.graphics.getHeight()*1/8));

        TextButton backButton = new TextButton("Main Menu",BlackJack.mySkin);
        backButton.setWidth(Gdx.graphics.getWidth()/8);
        backButton.setPosition(20,10);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Conformation.show(stage);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

        repeatButtonPlayerOne = new TextButton("Repeat Bet", BlackJack.mySkin);
        repeatButtonPlayerOne.setWidth(Gdx.graphics.getWidth()/12);
        repeatButtonPlayerOne.setPosition((Gdx.graphics.getWidth()/2-placeBetsButton.getWidth()/2) - 100 - ((Gdx.graphics.getWidth()/12 - Gdx.graphics.getWidth()/16)/2),(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2)+50);
        repeatButtonPlayerOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousPlayerOneBet > playerOne.getPlayerChip().bettingMoney + playerOne.getBetMoney() )
                {
                    errorMessage("not enough funds");
                    return;
                }
                if (previousPlayerOneBet < playerOne.getPlayerChip().bettingMoney)
                {
                    errorMessage("Your current bet is above your previous bet, please remove funds");
                    return;
                }
                addBetChips(countChips(previousPlayerOneBet - playerOne.getPlayerChip().bettingMoney), playerOne);
                playerOne.removeBetMoney(previousPlayerOneBet - playerOne.getPlayerChip().bettingMoney);
                playerOne.getPlayerChip().bettingMoney += previousPlayerOneBet - playerOne.getPlayerChip().bettingMoney ;
                displayBetTotal();

                allInButtonPlayerOne.remove();
                repeatButtonPlayerOne.remove();
                previousPlayerOneBet = playerOne.getPlayerChip().bettingMoney;

                playerOneBetReady = true;

                if (!playerTwoBetReady)
                {
                    return;
                }
                betsPlaced();
            };
        });

        repeatButtonPlayerTwo = new TextButton("Repeat Bet", BlackJack.mySkin);
        repeatButtonPlayerTwo.setWidth(Gdx.graphics.getWidth()/12);
        repeatButtonPlayerTwo.setPosition((Gdx.graphics.getWidth()/2+placeBetsButton.getWidth()/2) + 100 - Gdx.graphics.getWidth()/16 - ((Gdx.graphics.getWidth()/12 - Gdx.graphics.getWidth()/16)/2),(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2)+50);
        repeatButtonPlayerTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousPlayerTwoBet > playerTwo.getPlayerChip().bettingMoney + playerTwo.getBetMoney() )
                {
                    errorMessage("not enough funds");
                    return;
                }
                if (previousPlayerTwoBet < playerTwo.getPlayerChip().bettingMoney)
                {
                    errorMessage("Your current bet is above your previous bet, please remove funds");
                    return;
                }
                addBetChips(countChips(previousPlayerTwoBet - playerTwo.getPlayerChip().bettingMoney), playerTwo);
                playerTwo.removeBetMoney(previousPlayerTwoBet - playerTwo.getPlayerChip().bettingMoney);
                playerTwo.getPlayerChip().bettingMoney += previousPlayerTwoBet - playerTwo.getPlayerChip().bettingMoney ;
                displayBetTotal();

                allInButtonPlayerTwo.remove();
                repeatButtonPlayerTwo.remove();
                previousPlayerTwoBet = playerTwo.getPlayerChip().bettingMoney;

                playerTwoBetReady = true;

                if (!playerOneBetReady)
                {
                    return;
                }
                betsPlaced();
            };
        });

        allInButtonPlayerOne = new TextButton("ALL IN!", BlackJack.mySkin);
        allInButtonPlayerOne.setWidth(Gdx.graphics.getWidth()/16);
        allInButtonPlayerOne.setPosition((Gdx.graphics.getWidth()/2-placeBetsButton.getWidth()/2) - 100,(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2));
        allInButtonPlayerOne.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerOne.getPlayerChip().bettingMoney += playerOne.getBetMoney();
                playerOne.removeBetMoney(playerOne.getBetMoney());
                allIn(playerOne);
                displayBetTotal();

                allInButtonPlayerOne.remove();
                repeatButtonPlayerOne.remove();
                previousPlayerOneBet = playerOne.getPlayerChip().bettingMoney;

                playerOneBetReady = true;

                if (!playerTwoBetReady)
                {
                    return;
                }
                betsPlaced();
            };
        });


        allInButtonPlayerTwo = new TextButton("ALL IN!", BlackJack.mySkin);
        allInButtonPlayerTwo.setWidth(Gdx.graphics.getWidth()/16);
        allInButtonPlayerTwo.setPosition((Gdx.graphics.getWidth()/2+placeBetsButton.getWidth()/2) + 100 - Gdx.graphics.getWidth()/16,(Gdx.graphics.getHeight()/2- hitButton.getHeight()/2));
        allInButtonPlayerTwo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                playerTwo.getPlayerChip().bettingMoney += playerTwo.getBetMoney();
                playerTwo.removeBetMoney(playerTwo.getBetMoney());
                allIn(playerTwo);
                displayBetTotal();

                allInButtonPlayerTwo.remove();
                repeatButtonPlayerTwo.remove();
                previousPlayerTwoBet = playerTwo.getPlayerChip().bettingMoney;

                playerOneBetReady = true;

                if (!playerOneBetReady)
                {
                    return;
                }
                betsPlaced();

            };
        });

        TextButton infoButton = new TextButton("How to play",BlackJack.mySkin);
        infoButton.setWidth(Gdx.graphics.getWidth()/8);
        infoButton.setPosition(20,50);
        infoButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

                help.show(stage);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(infoButton);

        Conformation = new Dialog("Warning", BlackJack.mySkin, "dialog") {
            public void result(Object obj) {
                if (obj.equals(true))
                {
                    game.setScreen(new MenuScreen(game));
                }
            }
        };
        Conformation.text("Are you sure you want to go to main menu, game progress will be lost?");
        Conformation.button("Yes", true);
        Conformation.button("No", false);
        Conformation.key(Input.Keys.ENTER, true);
        Conformation.setMovable(false);

        saveDialog = new Dialog("Save", BlackJack.mySkin, "dialog") {
            public void result(Object obj) {
                if (obj.equals(true))
                {
                    saveGame(false);
                }
            }
        };
        saveDialog.text("Do you want to save your game progress?" +
                " Please note saving in the middle of a round results in loss of progress so save the game before you place your bet.");
        saveDialog.button("Yes", true);
        saveDialog.button("No", false);
        saveDialog.key(Input.Keys.ENTER, true);
        saveDialog.setMovable(false);

        readPreviousScenarios();



        if (menuScreenGameData.size== 1)
        {
            playerNameLabel.setText(NameScreen.getNameOne());
            player2NameLabel.setText(NameScreen.getNameTwo());
            help.show(stage);
        }

        else
        {
            Human[] playerSavedData = (Human[]) menuScreenGameData.get(1);
            Human loadedPlayerOne  = playerSavedData[0];
            Human loadedPlayerTwo  = playerSavedData[1];
            playerOne = loadedPlayerOne;
            playerTwo = loadedPlayerTwo;
            setPlayerChips();
            addNewChips(playerOne, playerOne.getBetMoney());
            addNewChips(playerTwo, playerOne.getBetMoney());
            playerNameLabel.setText(loadedPlayerOne.getName());
            player2NameLabel.setText(loadedPlayerTwo.getName());
            stage.addActor(playerNameLabel);
            stage.addActor(player2NameLabel);
            start = true;
            displayChipTotal();
            stage.addActor(placeBetsButton);
            stage.addActor(allInButtonPlayerOne);
            stage.addActor(allInButtonPlayerTwo);
        }


    }

    public void endRound()
    {
        dealFirstCard();
        standButton.remove();
        hitButton.remove();
        doubleButton.remove();
        if (playerOne.getPlayerChip().hasAce())
        {
            checkToAddAce(playerOne);
        }
        if (playerTwo.getPlayerChip().hasAce())
        {
            checkToAddAce(playerTwo);
        }
        playerStand = true;
        playerStand();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }


    @Override
    public void render(float delta) {

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

        stage.dispose();
    }

    public static Screen getScreen()
    {
        return screen;
    }

    private void setPlayerChips()
    {
        playerOne.setPlayerChip(40, (Gdx.graphics.getHeight()*1/4)+50, 130,(Gdx.graphics.getHeight()*1/4)+60,
                220,(Gdx.graphics.getHeight()*1/4)+70,130+(85/2)
                ,(Gdx.graphics.getHeight()*1/4)-90,130+(85/2) + 90,(Gdx.graphics.getHeight()*1/4)-100
                ,Gdx.graphics.getWidth()*1/4-220, (Gdx.graphics.getWidth()*1/4)-170, (Gdx.graphics.getWidth()*1/4)-120, (Gdx.graphics.getWidth()*1/4)-70, (Gdx.graphics.getWidth()*1/4)-20);
        playerTwo.setPlayerChip(Gdx.graphics.getWidth()-40-85, (Gdx.graphics.getHeight()*1/4)+50, Gdx.graphics.getWidth()-130-85,(Gdx.graphics.getHeight()*1/4)+60,
                (Gdx.graphics.getWidth())-220-85,(Gdx.graphics.getHeight()*1/4)+70,Gdx.graphics.getWidth()-(130+(85/2))-85,(Gdx.graphics.getHeight()*1/4)-90
                ,Gdx.graphics.getWidth()-85- (130+(85/2) + 90),(Gdx.graphics.getHeight()*1/4)-100, (Gdx.graphics.getWidth()*3/4)+230-85, (Gdx.graphics.getWidth()*3/4)+180-85
                ,(Gdx.graphics.getWidth()*3/4)+130-85, (Gdx.graphics.getWidth()*3/4)+90-85, (Gdx.graphics.getWidth()*3/4)+30-85);
    }

    private void displayChipTotal()
    {
        playerOneChips = new Label("" + playerOne.getBetMoney(), BlackJack.mySkin, "title");
        playerOneChips.setFontScale(1);
        playerOneChips.setPosition(Gdx.graphics.getWidth()*1/4-155, (Gdx.graphics.getHeight() * 1 / 4 - 33) );
        stage.addActor(playerOneChips);
        playerTwoChips = new Label("" + playerTwo.getBetMoney(), BlackJack.mySkin, "title");
        playerTwoChips.setFontScale(1);
        playerTwoChips.setPosition(Gdx.graphics.getWidth()*3/4+105, (Gdx.graphics.getHeight() * 1 / 4 - 33));
        stage.addActor(playerTwoChips);
    }

    private void betsPlaced()
    {
        placedBets = true;
        placeBetsButton.remove();
        allInButtonPlayerOne.remove();
        allInButtonPlayerTwo.remove();
        repeatButtonPlayerOne.remove();
        repeatButtonPlayerTwo.remove();
        PerfectPairButton.remove();
        flushButton.remove();
        previousPlayerOneBet = playerOne.getPlayerChip().bettingMoney;
        previousPlayerTwoBet = playerTwo.getPlayerChip().bettingMoney;
        startCards();
        hitButton.addAction(Actions.sequence(Actions.delay(4.7f), Actions.scaleTo(1,1,0.1f)));
        standButton.addAction(Actions.sequence(Actions.delay(4.7f), Actions.scaleTo(1,1,0.1f)));
        doubleButton.addAction(Actions.sequence(Actions.delay(4.7f), Actions.scaleTo(1,1,0.1f)));
        stage.addActor(hitButton);
        stage.addActor(standButton);
        stage.addActor(doubleButton);

    }

    public static Stage getStage()
    {
        return stage;
    }

    public static void saveGame(boolean quitApp)
    {
        if (fileChooser.getMode() == FileChooser.Mode.OPEN)
        {
            fileChooser.setMode(FileChooser.Mode.SAVE);
        }
        stage.addActor(fileChooser.fadeIn(2f));
        terminateApp = quitApp;

    }

    private void saveGameToFile(String filePath)
    {
        Human[] saveDetails = new Human[2];
        saveDetails[0] = playerOne;
        saveDetails[1] = playerTwo;
        try {
            String fileName = filePath;
            FileOutputStream fout= new FileOutputStream (fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(saveDetails);
            fout.close();
            oos.close();
            if (terminateApp)
            {
                System.exit(0);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void checkToAddAce(Player player)
    {

        if (player.getTotal() + 10 <=21)
        {
            player.add11Ace();
        }
    }

    public void startCards()
    {
        Card dealerCard = cardSet.getRandomCard();
        final Card dealerSecondCard = cardSet.getRandomCard();
        final Card playerCard = cardSet.getRandomCard();
        final Card playerSecondCard = cardSet.getRandomCard();
        Card player2Card = cardSet.getRandomCard();
        final Card player2SecondCard = cardSet.getRandomCard();
        playerOne.addCard(playerCard);
        playerOne.addCard(playerSecondCard);
        playerTwo.addCard(player2Card);
        playerTwo.addCard(player2SecondCard);
        dealer.addCard(dealerCard);
        dealer.addCard(dealerSecondCard);
        firstDealerCard = newCardImage (dealerCard.getCard());
        secondDealerCard = newCardImage(dealerSecondCard.getCard());
        Image firstPlayerCard = newCardImage(playerCard.getCard());
        Image secondPlayerCard = newCardImage(playerSecondCard.getCard());
        Image first2PlayerCard = newCardImage(player2Card.getCard());
        Image Second2PlayerCard = newCardImage(player2SecondCard.getCard());

        cardBack = newCardImage(new Texture("back.jpg"));
        dealFirstCard = newCardImage(new Texture("back.jpg"));

        cardBack.setName("card back");

        cardBack.setPosition(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160);
        dealFirstCard.setPosition(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160);
        firstPlayerCard.setPosition((Gdx.graphics.getWidth()/2)-240, (Gdx.graphics.getHeight()/8)+120);
        secondPlayerCard.setPosition((Gdx.graphics.getWidth()/2)-220, (Gdx.graphics.getHeight()/8)+120);
        first2PlayerCard.setPosition((Gdx.graphics.getWidth()/2)+60, (Gdx.graphics.getHeight()/8)+60);
        Second2PlayerCard.setPosition((Gdx.graphics.getWidth()/2)+80, (Gdx.graphics.getHeight()/8)+60);
        firstDealerCard.setPosition((Gdx.graphics.getWidth()/2)-90, (Gdx.graphics.getHeight()/2)+40);
        secondDealerCard.setPosition((Gdx.graphics.getWidth()/2)-70, (Gdx.graphics.getHeight()/2)+40);



        moveTo((Gdx.graphics.getWidth()/2)-240, (Gdx.graphics.getHeight()/8)+120,cardBack);
        cardBack.addAction(Actions.sequence(Actions.delay(0.5f),Actions.scaleTo(0.0f, 0, 0.1f),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f),Actions.scaleTo(1, 1, 0f)));
        firstPlayerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        firstPlayerCard.addAction(Actions.sequence(Actions.delay(0.6f), Actions.scaleTo(1,1,0.1f), Actions.rotateBy(-20,0.5F)));
        checkIfCardIsAce(playerCard, playerOne);

        cardBack.addAction(Actions.sequence(Actions.delay(0.7f),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(0.7f),Actions.scaleTo(1, 1, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(0.7f),run(new Runnable() {
            @Override
            public void run() {
                moveTo((Gdx.graphics.getWidth()/2)+60, (Gdx.graphics.getHeight()/8)+60, cardBack);
            }
        })));
        cardBack.addAction(Actions.sequence(Actions.delay(1.2f),Actions.scaleTo(0.0f, 0, 0.1f)));
        first2PlayerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        first2PlayerCard.addAction(Actions.sequence(Actions.delay(1.3f), Actions.scaleTo(1,1,0.1f),Actions.rotateBy(20,0.5f)));
        checkIfCardIsAce(player2Card, playerTwo);


        dealFirstCard.addAction(Actions.sequence(Actions.delay(1.4f),run(new Runnable() {
            @Override
            public void run() {
                moveTo((Gdx.graphics.getWidth()/2)-90, (Gdx.graphics.getHeight()/2)+40, dealFirstCard);
            }
        })));
        firstDealerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));

        cardBack.addAction(Actions.sequence(Actions.delay(1.9f),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(1.9f),Actions.scaleTo(1, 1, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(1.9f),run(new Runnable() {
            @Override
            public void run() {
                moveTo((Gdx.graphics.getWidth()/2)-220, (Gdx.graphics.getHeight()/8)+120, cardBack);
            }
        })));
        cardBack.addAction(Actions.sequence(Actions.delay(2.4f),Actions.scaleTo(0.0f, 0, 0.1f)));
        secondPlayerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        secondPlayerCard.addAction(Actions.sequence(Actions.delay(2.5f), Actions.scaleTo(1,1,0.1f),Actions.rotateBy(-20,0.5f)));
        checkIfCardIsAce(playerSecondCard, playerOne);
        playerPosition = (Gdx.graphics.getWidth()/2)-220;

        cardBack.addAction(Actions.sequence(Actions.delay(2.6f),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(2.6f),Actions.scaleTo(1, 1, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(2.6f),run(new Runnable() {
            @Override
            public void run() {
                moveTo((Gdx.graphics.getWidth()/2)+80, (Gdx.graphics.getHeight()/8)+60, cardBack);
            }
        })));
        cardBack.addAction(Actions.sequence(Actions.delay(3.1f),Actions.scaleTo(0.0f, 0, 0.1f)));
        Second2PlayerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        Second2PlayerCard.addAction(Actions.sequence(Actions.delay(3.2f), Actions.scaleTo(1,1,0.1f),Actions.rotateBy(20,0.5f)));
        checkIfCardIsAce(player2SecondCard, playerTwo);
        player2Position = (Gdx.graphics.getWidth()/2)+80;

        cardBack.addAction(Actions.sequence(Actions.delay(3.3f),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(3.3f),Actions.scaleTo(1, 1, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(3.3f),run(new Runnable() {
            @Override
            public void run() {
                moveTo((Gdx.graphics.getWidth()/2)-70, (Gdx.graphics.getHeight()/2)+40, cardBack);
            }
        })));
        cardBack.addAction(Actions.sequence(Actions.delay(3.8f),Actions.scaleTo(0.0f, 0, 0.1f)));
        secondDealerCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        secondDealerCard.addAction(Actions.sequence(Actions.delay(3.9f), Actions.scaleTo(1,1,0.1f),run(new Runnable() {
            @Override
            public void run() {
                playerNameLabel.setColor(Color.RED);
                int tempAddAceValuePlayerOne = 0;
                int tempAddAceValuePlayerTwo = 0;
                if (playerOne.getPlayerChip().hasAce())
                {
                    tempAddAceValuePlayerOne = playerOne.getTotal() + 10;
                    playerOneCardTotal.setText(playerOne.getTotal() + " or " + tempAddAceValuePlayerOne);
                }
                else
                {
                    playerOneCardTotal.setText(playerOne.getTotal()+"");
                }
                if (playerTwo.getPlayerChip().hasAce())
                {
                    tempAddAceValuePlayerTwo = playerTwo.getTotal() + 10;
                    playerTwoCardTotal.setText(playerTwo.getTotal() + " or " + tempAddAceValuePlayerTwo);
                }
                else
                {
                    playerTwoCardTotal.setText(playerTwo.getTotal()+"");
                }
                stage.addActor(playerOneCardTotal);
                stage.addActor(playerTwoCardTotal);
                if (tempAddAceValuePlayerOne>=21)
                {
                    playerOneOut = true;
                    player2Turn = true;
                    playerNameLabel.setColor(Color.WHITE);
                    player2NameLabel.setColor(Color.RED);
                }
                if (tempAddAceValuePlayerTwo>=21)
                {
                    playerTwoOut = true;
                    player2Turn = false;
                    player2NameLabel.setColor(Color.WHITE);
                }
                if (playerOneOut && playerTwoOut)
                {
                    endRound();
                }
                if (tutorialMode) {
                    actionButtonsPrompt.show(stage);
                }
                checkIfDealerHasAce(dealerSecondCard);
            }
        })));
        secondDealerCard.toFront();
        dealerPosition =  Gdx.graphics.getWidth()/2-70;
        firstCards = true;

        if (dealerCard.getNumber() ==1 || dealerSecondCard.getNumber() == 1)
        {
            dealerHadAce = true;
        }


    }

    private void refreshCardTotal()
    {
        int tempAddAceValue;
        if (playerOne.getPlayerChip().hasAce())
        {
            tempAddAceValue = playerOne.getTotal() + 10;
            playerOneCardTotal.setText(playerOne.getTotal() + " or " + tempAddAceValue);
        }
        else
        {
            playerOneCardTotal.setText(playerOne.getTotal()+"");
        }
        if (playerTwo.getPlayerChip().hasAce())
        {
            tempAddAceValue = playerTwo.getTotal() + 10;
            playerTwoCardTotal.setText(playerTwo.getTotal() + " or " + tempAddAceValue);
        }
        else
        {
            playerTwoCardTotal.setText(playerTwo.getTotal()+"");
        }
    }

    private boolean checkFirstCard()
    {
        hitButton.remove();
        standButton.remove();
        doubleButton.remove();
        dealFirstCard.addAction(Actions.sequence(Actions.rotateBy(360, 1f), run(new Runnable() {
            @Override
            public void run() {
                if (dealer.getTotal() == 21 || dealer.getTotal() == 11 && dealerHadAce)
                {
                    dealFirstCard();
                    checkFor21 = true;
                }
                else {
                    if (!stage.getActors().contains(endOfRoundDialog, true))
                    {
                        stage.addActor(hitButton);
                        stage.addActor(standButton);
                        stage.addActor(doubleButton);
                    }


                }
                insuranceOutCome();

            }
        })));
        return checkFor21;
    }

    private void moveTo(int w, int h, Image i)
    {
        if (cardMove.isPlaying())
        {
            cardMove.stop();
        }
        cardMove.play();
        i.addAction(Actions.moveTo(w, h, 0.5f));

    }


    public void dealFirstCard()
    {
        dealFirstCard.addAction(Actions.sequence(Actions.scaleTo(0.0f, 0, 0.1f)));
        firstDealerCard.addAction(Actions.sequence(Actions.delay(0.1f), Actions.scaleTo(1,1,0.1f)));
    }

    public void newPlayerCard(final Human player)
    {

        firstCards = false;
        int positionX = 0;
        int positionY = 0;
        int rotate = 0;
        Card card = cardSet.getRandomCard();
        if (player == playerOne)
        {
            playerPosition += 20;
            playerOne.addCard(card);
            positionX = playerPosition;
            positionY = (Gdx.graphics.getHeight()/8)+120;
            rotate = -20;
        }
        else
        {
            playerTwo.addCard(card);
            player2Position += 20;
            positionX = player2Position;
            positionY = (Gdx.graphics.getHeight()/8)+60;
            rotate = 20;
        }
        checkIfCardIsAce(card, player);
        Image addCard = new Image(card.getCard());
        addCard.setSize(120,150);
        addCard.setPosition(positionX, positionY);
        stage.addActor(addCard);
        cardBack.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()-160, 0f)));
        cardBack.addAction(Actions.sequence(Actions.delay(0f),Actions.scaleTo(1, 1, 0f)));
        moveTo(positionX, positionY, cardBack);
        cardBack.addAction(Actions.sequence(Actions.delay(0.5f),Actions.scaleTo(0.0f, 0, 0.1f)));
        addCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        addCard.addAction(Actions.sequence(Actions.delay(0.6f), Actions.scaleTo(1,1,0.1f),Actions.rotateBy(rotate, 0.5f),run(new Runnable() {
            @Override
            public void run() {
                refreshCardTotal();
                if (player.getTotal() == 11 && player.getPlayerChip().hasAce()) {
                    checkToAddAce(player);
                }
                if (player.getTotal()>=21  )
                {
                    checkScore();
                }
            }
        })));


    }



    private void checkIfCardIsAce(Card card, Human player)
    {
        if (card.getNumber() == 1)
        {
            player.getPlayerChip().setAce();
        }
    }

    private void playerStand()
    {
        float x =0;
        if (checkIfDealerNeedsNewCard() || dealer.getTotal() < playerOne.getTotal() || dealer.getTotal() < playerTwo.getTotal()) {

            while (((playerOne.getTotal() > dealer.getTotal() && dealer.getTotal() <= 21) || (playerTwo.getTotal() > dealer.getTotal() && dealer.getTotal() <= 21)) ) {
                if (dealerHadAce && dealer.getTotal() == 11 || dealer.getTotal() ==21)
                {
                    return;
                }

                x = newDealerCard(x);
                if (!firstRound)
                {
                    break;
                }
            }
        }
        new Timer().schedule(
                new Timer.Task() {
                    @Override
                    public void run() {
                        checkScore();
                    }
                },
                x + 1f
        );
    }
    private boolean checkIfDealerNeedsNewCard()
    {
        if (dealer.getTotal() == 11 && dealerHadAce || dealer.getTotal() == 21   )
        {
            return false;
        }
        boolean check = checkPreviousCardsStand();
        if (check)
        {
            return true;
        }
        int playerDifference = 21 - playerOne.getTotal();
        int dealerDifference = 21 - dealer.getTotal();
        Double playerBuffProb = cardSet.getBustProbability(playerDifference);
        Double dealerBuffProb = cardSet.getBustProbability(dealerDifference);
        if (dealerBuffProb == 0 || playerBuffProb > dealerBuffProb)
        {
            return true;
        }
        return false;
    }

    private void addButtons(float x)
    {
        new Timer().schedule(
                new Timer.Task() {
                    @Override
                    public void run() {
                        if (!stage.getActors().contains(endOfRoundDialog, true))
                        {
                            stage.addActor(hitButton);
                            stage.addActor(standButton);
                            if (firstRound == true)
                            {
                                stage.addActor(doubleButton); }
                        }

                    }
                },
                x
        );
    }

    private boolean checkPreviousCardsStand()
    {
        try {
            Logger logObject = previousScenarios[playerOne.getTotal() > 21 ? 0 : playerOne.getTotal()][playerTwo.getTotal() > 21 ? 0 : playerTwo.getTotal()][dealer.getTotal() > 21 ? 0 : dealer.getTotal()];
            if (logObject == null)
            {
                return false;
            }
            return dealer.computeHit(logObject);
        }
        catch (Exception e)
        {
            return false;
        }

    }



    public float newDealerCard(float x)
    {

        dealerPosition += 20;
        Card card = cardSet.getRandomCard();
        dealer.addCard(card);
        Image addCard = new Image(card.getCard());
        addCard.setSize(120,150);
        addCard.setPosition(dealerPosition, (Gdx.graphics.getHeight()/2)+40);
        stage.addActor(addCard );
        Image newCardBack = cardBack;
        stage.addActor(newCardBack);
        newCardBack.addAction(Actions.sequence(Actions.delay(x),Actions.moveTo(Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight()+160, 0f)));
        newCardBack.addAction(Actions.sequence(Actions.delay(x),Actions.scaleTo(1, 1, 0f)));
        newCardBack.addAction(Actions.sequence(Actions.delay(x),Actions.moveTo(dealerPosition, (Gdx.graphics.getHeight()/2)+40, 1f)));
        newCardBack.addAction(Actions.sequence(Actions.delay(x=x+1f),Actions.scaleTo(0, 0, 0.1f)));
        addCard.addAction(Actions.sequence(Actions.scaleTo(0,1)));
        addCard.addAction(Actions.sequence(Actions.delay(x=x+0.1f), Actions.scaleTo(1,1,0.1f)));
        x =x+0.1f;
        return x;

    }

    private void checkScore()
    {
        int p1Total = playerOne.getTotal();
        int p2Total = playerTwo.getTotal();
        int dTotal = dealer.getTotal();
        if (p2Total == 21 && !playerOneOut)
        {
            playerTwoOut = true;
            return;
        }
        if (p1Total == 21 && !playerTwoOut)
        {
            playerOneOut = true;
            return;
        }
        if (p1Total>21)
        {
            playerOneOut = true;
            if (playerOneOut && playerTwoOut) {
                hitButton.remove();
                standButton.remove();
                if (p2Total == 21 && dTotal !=21)
                {
                    displayWinner("p2");
                }
                else if (p2Total < dTotal && p2Total <=21)
                {
                    displayWinner("p2");
                }
                else if (p2Total == dTotal)
                {
                    displayWinner("p2s");
                }
                else
                {
                    displayWinner("dealer");
                }
            }
            return;
        }

        if (p2Total>21)
        {
            playerTwoOut = true;
            if (playerOneOut && playerTwoOut) {
                if (p1Total < dTotal && p1Total <=21)
                {
                    displayWinner("p1");
                }
                else if (p1Total == dTotal)
                {
                    displayWinner("p1s");
                }
                else
                {
                    displayWinner("dealer");
                }
            }
            return;
        }

        if (dTotal>21)
        {
            if (p1Total<= 21)
            {
                if (p2Total <= 21)
                {
                    displayWinner("P");
                    return;
                }
                displayWinner("p1");
                return;
            }
            if (p2Total <= 21)
            {
                displayWinner("p2");
                return;
            }
            return;
        }
        if ((dTotal == p1Total) && dTotal  == p2Total)
        {
            displayWinner("s");
            return;
        }
        if (dTotal<=21)
        {
            if (dTotal>p1Total  && dTotal > p2Total)
            {
                displayWinner("dealer");
                return;
            }
            else if (p1Total > dTotal && p2Total > dTotal)
            {
                displayWinner("P");
                return;
            }
            else if (p1Total < dTotal && p2Total == dTotal)
            {
                displayWinner("p2s");
                return;
            }
            else if (p2Total < dTotal && p1Total == dTotal)
            {
                displayWinner("p1s");
                return;
            }

        }
    }

    public boolean checkFirstScore()
    {
        gameEnd = false;
        if (firstCards && playerOne.getTotal()== 21)
        {
            dealFirstCard();
            dealFirstCard.addAction(Actions.sequence(Actions.delay(0.1f),run(new Runnable() {
                @Override
                public void run() {
                    if (dealer.getTotal() == 21)
                    {
                        displayWinner("s");
                        gameEnd = true;
                    }
                    displayWinner("P");
                    gameEnd = true;
                }
            })));
        }
        return gameEnd;
    }


    private void clearCards(Array array)
    {
        if (array.size == 0)
        {
            return;
        }
        Object obj = array.first();
        if (obj instanceof Image)
        {
            if (((Image) obj).getName() =="card deck" || ((Image) obj).getName() =="chip" || ((Image) obj).getName() == "backgroundSoundImage")
            {
                Array a = new Array();
                a.addAll(array,1,array.size-1);
                clearCards(a);
                return;
            }
            array.removeIndex(0);
            ((Image) obj).remove();
            clearCards(array);
            return;
        }
        else
        {
            Array a = new Array();
            a.addAll(array,1,array.size-1);
            clearCards(a);
            return;
        }
    }

    private void displayWinner(final String name)
    {
        String text = "";
        if (name.equals("dealer"))
        {
            saveScenarios(true);
            text = "Dealer wins";
        }
        else if (name.equals("s"))
        {
            saveScenarios(false);
            text = "Stand off!";
            playerOne.addBetMoney(playerOne.getPlayerChip().bettingMoney);
            playerTwo.addBetMoney(playerTwo.getPlayerChip().bettingMoney);
        }

        else if (name.equals("p1s"))
        {
            saveScenarios(false);
            text = NameScreen.getNameOne() + " You have had a Stand off with the dealer!";
            playerOne.addBetMoney(playerOne.getPlayerChip().bettingMoney);
        }
        else if (name.equals("p2s"))
        {
            saveScenarios(false);
            text = NameScreen.getNameTwo() + " You have had a Stand off with the dealer!";
            playerTwo.addBetMoney(playerTwo.getPlayerChip().bettingMoney);
        }
        else if (name.equals("p1"))
        {
            saveScenarios(false);
            text = NameScreen.getNameOne() + " You have won!";
            playerOne.addBetMoney(playerOne.getPlayerChip().bettingMoney*3/2);
        }
        else if (name.equals("p2"))
        {
            saveScenarios(false);
            text = NameScreen.getNameTwo() + " You have won!";
            playerTwo.addBetMoney(playerTwo.getPlayerChip().bettingMoney*3/2);
        }
        else
        {
            saveScenarios(false);
            text = "You have both Won!";
            playerOne.addBetMoney(playerOne.getPlayerChip().bettingMoney*3/2);
            playerTwo.addBetMoney(playerTwo.getPlayerChip().bettingMoney*3/2);

        }
        text += " \n Click to start the next round";
        hitButton.remove();
        doubleButton.remove();
        standButton.remove();
        endOfRoundDialog = new Dialog("End of Round", BlackJack.mySkin);
        final Label heading = new Label(text, BlackJack.mySkin, "title-plain");
        endOfRoundDialog.text(heading);
        endOfRoundDialog.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
        endOfRoundDialog.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (name.equals("p1s")  )
                {
                    returnChipsToPlayer(playerOne, null);
                    returnChipsToBank(null);
                }
                else if (name.equals("p2s") )
                {
                    returnChipsToPlayer(playerTwo, null);
                    returnChipsToBank(null);
                }
                else if (name.equals("p1"))
                {
                    returnChipsToPlayer(playerOne, null);
                    returnChipsToBank(null);
                    addNewChips(playerOne, (playerOne.getPlayerChip().bettingMoney * 3/2)-playerOne.getPlayerChip().bettingMoney);
                }
                else if (name.equals("p2"))
                {
                    returnChipsToPlayer(playerTwo, null);
                    returnChipsToBank(null);
                    addNewChips(playerTwo, (playerTwo.getPlayerChip().bettingMoney * 3/2)-playerTwo.getPlayerChip().bettingMoney);
                }
                else if (name.equals("s"))
                {
                    returnChipsToPlayer(playerOne, null);
                    returnChipsToPlayer(playerTwo, null);
                }
                else if (name.equals("P"))
                {
                    returnChipsToPlayer(playerOne, null);
                    returnChipsToPlayer(playerTwo, null);
                    addNewChips(playerOne, (playerOne.getPlayerChip().bettingMoney * 3/2)-playerOne.getPlayerChip().bettingMoney);
                    addNewChips(playerTwo, (playerTwo.getPlayerChip().bettingMoney * 3/2)-playerTwo.getPlayerChip().bettingMoney);
                }
                else
                {
                    returnChipsToBank(null);
                }
                endOfRoundDialog.remove();
                resetLayout();
            };
        });
        endOfRoundDialog.show(stage);
        endOfRoundDialog.setMovable(false);
    }

    private void returnChipsToPlayer(Human player, ArrayList<ArrayList<Image>> arrayList)
    {
        ArrayList<Image> chipsHundredArray = betChipsHundred;
        ArrayList<Image> chipsTwentyFiveArray = betChipsTwentyFive;
        ArrayList<Image> chipsTenArray = betChipsTen;
        ArrayList<Image> chipsFiveArray = betChipsFive;
        ArrayList<Image> chipsOneArray = betChipsOne;

        if (arrayList != null)
        {
            chipsHundredArray = arrayList.get(4);
            chipsTwentyFiveArray = arrayList.get(3);
            chipsTenArray = arrayList.get(2);
            chipsFiveArray = arrayList.get(1);
            chipsOneArray = arrayList.get(0);
        }

        for (int i = 0; i < chipsHundredArray.size() ; i++)
        {
            chipMove.play();
            Object obj = chipsHundredArray.get(i);
            Image image = (Image) obj;
            float x = image.getOriginX();
            if (x == player.getPlayerChip().hundredChipX)
            {
                chipsHundredArray.remove(i);
                i--;
                image.setName("chip");
                image.setSize(85,85);
                image.addAction(Actions.sequence(Actions.moveTo(x, player.getPlayerChip().hundredChipY, 0.3f)));
                image.toFront();
                image.setUserObject(false);
                image.setOriginY(player.getPlayerChip().hundredChipY);
                player.getPlayerChip().hundredChipY += 3;
            }
        }
        for (int i = 0; i < chipsTwentyFiveArray.size() ; i++)
        {
            chipMove.play();
            Object obj = chipsTwentyFiveArray.get(i);
            Image image = (Image) obj;
            float x = image.getOriginX();
            if (x == player.getPlayerChip().twentyFiveChipX)
            {
                chipsTwentyFiveArray.remove(i);
                i--;
                image.setName("chip");
                image.setSize(85,85);
                image.addAction(Actions.sequence(Actions.moveTo(x, player.getPlayerChip().twentyFiveChipY, 0.3f)));
                image.toFront();
                image.setUserObject(false);
                image.setOriginY(player.getPlayerChip().twentyFiveChipY);
                player.getPlayerChip().twentyFiveChipY += 3;
            }
        }
        for (int i = 0; i < chipsTenArray.size() ; i++)
        {
            chipMove.play();
            Object obj = chipsTenArray.get(i);
            Image image = (Image) obj;
            float x = image.getOriginX();
            if (x == player.getPlayerChip().tenChipX)
            {
                chipsTenArray.remove(i);
                i--;
                image.setName("chip");
                image.setSize(85,85);
                image.addAction(Actions.sequence(Actions.moveTo(x, player.getPlayerChip().tenChipY, 0.3f)));
                image.toFront();
                image.setUserObject(false);
                image.setOriginY(player.getPlayerChip().tenChipY);
                player.getPlayerChip().tenChipY += 3;
            }
        }
        for (int i = 0; i < chipsFiveArray.size() ; i++)
        {
            chipMove.play();
            Object obj = chipsFiveArray.get(i);
            Image image = (Image) obj;
            float x = image.getOriginX();
            if (x == player.getPlayerChip().fiveChipX)
            {
                chipsFiveArray.remove(i);
                i--;
                image.setName("chip");
                image.setSize(85,85);
                image.addAction(Actions.sequence(Actions.moveTo(x, player.getPlayerChip().fiveChipY, 0.3f)));
                image.toFront();
                image.setUserObject(false);
                image.setOriginY(player.getPlayerChip().fiveChipY);
                player.getPlayerChip().fiveChipY += 3;
            }
        }
        for (int i = 0; i < chipsOneArray.size() ; i++)
        {
            chipMove.play();
            Object obj = chipsOneArray.get(i);
            Image image = (Image) obj;
            float x = image.getOriginX();
            if (x == player.getPlayerChip().oneChipX)
            {
                chipsOneArray.remove(i);
                i--;
                image.setName("chip");
                image.setSize(85,85);
                image.addAction(Actions.sequence(Actions.moveTo(x, player.getPlayerChip().oneChipY, 0.3f)));
                image.toFront();
                image.setUserObject(false);
                image.setOriginY(player.getPlayerChip().oneChipY);
                player.getPlayerChip().oneChipY += 3;
            }
        }


    }

    private void returnChipsToBank(ArrayList<ArrayList<Image>> arrayList)
    {
        ArrayList<Image> chipsHundredArray = betChipsHundred;
        ArrayList<Image> chipsTwentyFiveArray = betChipsTwentyFive;
        ArrayList<Image> chipsTenArray = betChipsTen;
        ArrayList<Image> chipsFiveArray = betChipsFive;
        ArrayList<Image> chipsOneArray = betChipsOne;

        if (arrayList != null)
        {
            chipsHundredArray = arrayList.get(4);
            chipsTwentyFiveArray = arrayList.get(3);
            chipsTenArray = arrayList.get(2);
            chipsFiveArray = arrayList.get(1);
            chipsOneArray = arrayList.get(0);
        }
        while (chipsHundredArray.size() !=0)
        {
            chipMove.play();
            final Object obj = chipsHundredArray.get(0);
            ((Image) obj).addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()*1/128 + 90 + 90,Gdx.graphics.getHeight()-85,0.3f),run(new Runnable() {
                @Override
                public void run() {
                    ((Image) obj).remove();
                }
            })));
            chipsHundredArray.remove(0);
        }
        while (chipsTwentyFiveArray.size() !=0)
        {
            chipMove.play();
            final Object obj = chipsTwentyFiveArray.get(0);
            ((Image) obj).addAction(Actions.sequence(Actions.moveTo((Gdx.graphics.getWidth()*1/128)+90,Gdx.graphics.getHeight()-175,0.3f),run(new Runnable() {
                @Override
                public void run() {
                    ((Image) obj).remove();
                }
            })));
            chipsTwentyFiveArray.remove(0);
        }
        while (chipsTenArray.size() !=0)
        {
            chipMove.play();
            final Object obj = chipsTenArray.get(0);
            ((Image) obj).addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()*1/128,Gdx.graphics.getHeight()-175,0.3f),run(new Runnable() {
                @Override
                public void run() {
                    ((Image) obj).remove();
                }
            })));
            chipsTenArray.remove(0);
        }
        while (chipsFiveArray.size() !=0)
        {
            chipMove.play();
            final Object obj = chipsFiveArray.get(0);
            ((Image) obj).addAction(Actions.sequence(Actions.moveTo((Gdx.graphics.getWidth()*1/128)+90, Gdx.graphics.getHeight()-85,0.3f),run(new Runnable() {
                @Override
                public void run() {
                    ((Image) obj).remove();
                }
            })));
            chipsFiveArray.remove(0);
        }
        while (chipsOneArray.size() !=0)
        {
            chipMove.play();
            final Object obj = chipsOneArray.get(0);
            ((Image) obj).addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()*1/128, Gdx.graphics.getHeight()-85,0.3f),run(new Runnable() {
                @Override
                public void run() {
                    ((Image) obj).remove();
                }
            })));
            chipsOneArray.remove(0);
        }
    }

    private void checkIfDealerHasAce(Card card)
    {
        if (card.getNumber() != 1 || playerOne.getBetMoney() < playerOne.getPlayerChip().bettingMoney/2 && playerTwo.getBetMoney() < playerTwo.getPlayerChip().bettingMoney/2)
        {
            return;
        }
        final Dialog aceConformation = new Dialog("ACE", BlackJack.mySkin, "dialog") {
            public void result(Object obj) {
                if (!(obj.equals(false)))
                {
                    standButton.remove();
                    hitButton.remove();
                    doubleButton.remove();
                    if ( obj instanceof Human)
                    {
                        Human player = ((Human)obj);
                        player.removeBetMoney(player.getPlayerChip().bettingMoney/2);
                        if (player == playerOne) {
                            playerInsurance = true;
                        }
                        else
                        {
                            player2Insurance = true;
                        }
                        insuranceChips(player);
                    }
                    else if (obj.equals( true))
                    {
                        playerOne.removeBetMoney(playerOne.getPlayerChip().bettingMoney/2);
                        playerTwo.removeBetMoney(playerTwo.getPlayerChip().bettingMoney/2);
                        playerInsurance = true;
                        player2Insurance = true;
                        insuranceChips(playerTwo);
                        insuranceChips(playerOne);

                    }
                    refreshInsuranceBetLabels();
                    displayBetTotal();
                }
                checkFirstCard();
            }
        };
        aceConformation.text("Dealer has an ace \n select below if you want insurance? \n payout is 2 to 1");
        int bothPlayersCanGetAce = 0;
        if (playerOne.getBetMoney() > playerOne.getPlayerChip().bettingMoney/2)
        {
            bothPlayersCanGetAce++;
            aceConformation.button(NameScreen.getNameOne(), playerOne);
        }
        if (playerTwo.getBetMoney() > playerTwo.getPlayerChip().bettingMoney/2)
        {
            bothPlayersCanGetAce++;
            aceConformation.button(NameScreen.getNameTwo(), playerTwo);
        }
        if (bothPlayersCanGetAce == 2) {
            aceConformation.button(NameScreen.getNameOne() + " " + NameScreen.getNameTwo(), true);
        }
        aceConformation.button("no", false);
        if (tutorialMode)
        {
            final Dialog help = new Dialog("Insurance", BlackJack.mySkin);
            final Label heading = new Label("insurance pays 2 to 1 \n" +
                    "when taking out insurance you bet half your original bet \n if the dealer checks the hidden card \n " +
                    "if they have 21 you get all your money back and the round ends \n if not the game continues as normal \n click to hide", BlackJack.mySkin, "title-plain");
            help.text(heading);
            help.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
            help.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    help.hide();
                    aceConformation.show(stage);
                };
            });
            help.setMovable(false);
            help.show(stage);
        }
        else
        {
            aceConformation.show(stage);
        }
        aceConformation.setMovable(false);
    }

    private Double[] countChips(int amount)
    {
        Double twentyFives = 0.0;
        Double tens = 0.0;
        Double fives = 0.0;
        Double ones = 0.0;
        double hundreds = Math.floor(amount/100);
        amount = amount - new Double(hundreds*100).intValue();
        if (amount >0)
        {
            twentyFives = Math.floor(amount/25);
            amount = amount - new Double(twentyFives*25).intValue();
            if (amount >0)
            {
                tens = Math.floor(amount/10);
                amount = amount - new Double(tens*10).intValue();
                if (amount >0)
                {
                    fives = Math.floor(amount/5);
                    amount = amount - new Double(fives*5).intValue();
                    if (amount >0)
                    {
                        ones = Math.floor(amount);
                    }
                }
            }
        }
        return new Double[]{hundreds, twentyFives, tens, fives, ones};
    }

    private void allIn(Human player)
    {
        PlayerChip playerChip = player.getPlayerChip();
        double hundredChips = (playerChip.hundredChipY -3 - (Gdx.graphics.getHeight()*1/4)-100)/3;
        double twentyFivechips = (playerChip.twentyFiveChipY -3 - (Gdx.graphics.getHeight()*1/4)-90)/3;
        double tenChips = (playerChip.tenChipY - 3 - (Gdx.graphics.getHeight()*1/4)+70)/3;
        double fiveChips = (playerChip.fiveChipY - 3 - (Gdx.graphics.getHeight()*1/4)+60)/3;
        double oneChips = (playerChip.oneChipY - 3 - (Gdx.graphics.getHeight()*1/4)+50)/3;
        Double[] chipCount = {hundredChips,twentyFivechips,tenChips,fiveChips,oneChips};
        addBetChips(chipCount, player);

    }

    private void refreshInsuranceBetLabels()
    {
        if (playerInsurance)
        {
            playerOneInsuranceLabel.setText(""+playerOne.getPlayerChip().bettingMoney /2 );
        }
        if (player2Insurance)
        {
            playerTwoInsuranceLabel.setText(""+playerTwo.getPlayerChip().bettingMoney /2 );
        }
    }

    private void addBetChips(Double[] chipCount, Human player)
    {
        Array array = stage.getActors();
        Double twentyFives = chipCount[1];
        Double tens = chipCount[2];
        Double fives = chipCount[3];
        Double ones = chipCount[4];
        Double hundreds = chipCount[0];
        PlayerChip playerData = player.getPlayerChip();


        int i = array.size-1;


        while (hundreds!=0 || twentyFives!=0 || tens!=0 || fives!=0 || ones!=0)
        {
            if (i == 0)
            {
                break;
            }
            Object obj = array.get(i);
            if (obj instanceof Image)
            {
                chipMove.play();
                Image image = (Image) obj;
                if (image.getY() == (playerData.hundredChipY-3) && image.getX() == playerData.hundredChipX && hundreds!=0)
                {
                    betChipsHundred.add(image);
                    playerData.betHeightHundred += 3;
                    playerData.hundredChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(playerData.betHundredPosX,playerData.betHeightHundred,0.3f));
                    hundreds--;
                }
                else if (image.getY() == (playerData.twentyFiveChipY-3) && image.getX() == playerData.twentyFiveChipX && twentyFives!=0)
                {
                    betChipsTwentyFive.add(image);
                    playerData.betHeightTwentyFive +=3;
                    playerData.twentyFiveChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(playerData.betTwentyFivePosX,playerData.betHeightTwentyFive,0.3f));
                    twentyFives--;
                }
                else if (image.getY() == playerData.tenChipY-3 && image.getX() == playerData.tenChipX && tens!=0)
                {
                    betChipsTen.add(image);
                    playerData.betHeightTen +=3;
                    playerData.tenChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(playerData.betTenPosX,playerData.betHeightTen,0.3f));
                    tens--;
                }
                else if (image.getY() == playerData.fiveChipY-3 && image.getX() == playerData.fiveChipX && fives!=0)
                {
                    betChipsFive.add(image);
                    playerData.betHeightFive +=3;
                    playerData.fiveChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(playerData.betFivePosX,playerData.betHeightFive,0.3f));
                    fives--;
                }
                else if (image.getY() == playerData.oneChipY-3 && image.getX() == playerData.oneChipX && ones!=0)
                {
                    betChipsOne.add(image);
                    playerData.betHeightOne += 3;
                    playerData.oneChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(playerData.betOnePosX,playerData.betHeightOne,0.3f));
                    ones--;
                }
                fixChips(null);
            }
            i--;
        }
    }

    private void doubleChips(Human player)
    {

        Double[] chipCount = countChips(player.getPlayerChip().bettingMoney);
        addBetChips(chipCount, player);
    }

    private void insuranceChips(Human player)
    {

        Array array = stage.getActors();
         Double[] chipCount = countChips(player.getPlayerChip().bettingMoney/2);
         Double twentyFives = chipCount[1];
         Double tens = chipCount[2];
         Double fives = chipCount[3];
         Double ones = chipCount[4];
         Double hundreds = chipCount[0];
         PlayerChip playerData = player.getPlayerChip();
        int i = array.size-1;
        while (hundreds!=0 || twentyFives!=0 || tens!=0 || fives!=0 || ones!=0)
        {
            if (i < 0)
            {
                break;
            }
            Object obj = array.get(i);
            if (obj instanceof Image)
            {
                chipMoveBottom.play();
                Image image = (Image) obj;
                if (image.getY() == (playerData.hundredChipY-3) && image.getX() == playerData.hundredChipX && hundreds>0)
                {
                    playerData.insuranceArray.get(4).add(image);
                    playerData.hundredChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(player == playerOne ? 280 : 445,(Gdx.graphics.getHeight()*3/4)+103,0.3f));
                    hundreds--;
                }
                else if (image.getY() == playerData.twentyFiveChipY -3 && image.getX() == playerData.twentyFiveChipX && twentyFives!=0)
                {
                    playerData.insuranceArray.get(3).add(image);
                    playerData.twentyFiveChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(player == playerOne ? 300: 465,(Gdx.graphics.getHeight()*3/4)+103,0.3f));
                    twentyFives--;
                }
                else if (image.getY() == playerData.tenChipY-3 && image.getX() == playerData.tenChipX && tens!=0)
                {
                    playerData.insuranceArray.get(2).add(image);
                    playerData.tenChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(player == playerOne ? 320: 485,(Gdx.graphics.getHeight()*3/4)+103,0.3f));
                    tens--;
                }
                else if (image.getY() == playerData.fiveChipY-3 && image.getX() == playerData.fiveChipX && fives!=0)
                {
                    playerData.insuranceArray.get(1).add(image);
                    playerData.fiveChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(player == playerOne ? 340: 505,(Gdx.graphics.getHeight()*3/4)+103,0.3f));
                    fives--;
                }
                else if (image.getY() == playerData.oneChipY-3 && image.getX() == playerData.oneChipX && ones!=0)
                {
                    playerData.insuranceArray.get(0).add(image);
                    playerData.oneChipY -= 3;
                    image.setSize(70,70);
                    image.addAction(Actions.moveTo(player == playerOne ? 360 : 525,(Gdx.graphics.getHeight()*3/4)+103,0.3f));
                    ones--;
                }

            }
            i--;
        }
        if (!stage.getActors().contains(hitButton, true))
        {
            firstRound = true;
            addButtons(0.3f);
        }

    }

    private void showChips()
    {

        for (int i = 0;i<6;i++)
        {
            addHundredChip(playerOne);
            addHundredChip(playerTwo);
        }
        for (int j =0;j<8;j++)
        {
            addTwentyFiveChip(playerOne);
            addTwentyFiveChip(playerTwo);
        }
        for (int k = 0;k<10;k++)
        {
            addTenChip(playerOne);
            addTenChip(playerTwo);
        }
        for (int l = 0;l<18;l++)
        {
            addFiveChip(playerOne);
            addFiveChip(playerTwo);
        }
        for (int l = 0;l<10;l++)
        {
            addOneChip(playerOne);
            addOneChip(playerTwo);
        }

    }

    private void displayBetTotal()
    {
        playerOneChips.setText(""+ playerOne.getBetMoney());
        playerTwoChips.setText(""+playerTwo.getBetMoney());
        if (playerOne.getPlayerChip().bettingMoney > 0)
        {
            playerBetTotal.setText("Bet total: \n     " + playerOne.getPlayerChip().bettingMoney);
            stage.addActor(playerBetTotal);
        }
        else
        {
            playerBetTotal.remove();
        }
        if (playerTwo.getPlayerChip().bettingMoney > 0)
        {
            playerBetTotal2.setText("Bet total: \n     " +playerTwo.getPlayerChip().bettingMoney);
            playerBetTotal2.setPosition((Gdx.graphics.getWidth() * 3 / 4)-93  ,(Gdx.graphics.getHeight()*1/3)+140);
            stage.addActor(playerBetTotal2);
        }
        else
        {
            playerBetTotal2.remove();
        }
        if (playerOne.getPlayerChip().perfectPairMoney > 0)
        {
            playerOnePerfectPairBetLabel.setText("" + playerOne.getPlayerChip().perfectPairMoney);
        }
        else
        {
            playerOnePerfectPairBetLabel.setText("");
        }
        if (playerTwo.getPlayerChip().perfectPairMoney > 0)
        {
            playerTwoPerfectPairBetLabel.setText("" + playerTwo.getPlayerChip().perfectPairMoney);
        }
        else
        {
            playerTwoPerfectPairBetLabel.setText("");
        }
        if (playerOne.getPlayerChip().flushMoney > 0)
        {
            playerOneFlushBetLabel.setText("" + playerOne.getPlayerChip().flushMoney);
        }
        else
        {
            playerOneFlushBetLabel.setText("");
        }
        if (playerTwo.getPlayerChip().flushMoney > 0)
        {
            playerTwoFlushBetLabel.setText("" + playerTwo.getPlayerChip().flushMoney);
        }
        else
        {
            playerTwoFlushBetLabel.setText("");
        }
    }


    public void errorMessage(String message)
    {
        noBet = new Dialog("ERROR", BlackJack.mySkin);
        noBet.setY(Gdx.graphics.getHeight()*1/2);
        noBet.setWidth(Gdx.graphics.getWidth());
        Label heading = new Label(message, BlackJack.mySkin, "title-plain");
        noBet.text(heading);
        noBet.show(stage);
        noBet.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                noBet.hide();
            };
        });

    }

    private void addNewChips(Human player, int amount)
    {

        Double[] chipCount = countChips(amount);
        Double twentyFives = chipCount[1];
        Double tens = chipCount[2];
        Double fives = chipCount[3];
        Double ones = chipCount[4];
        Double hundreds = chipCount[0];
        for (int i =0;i<hundreds;i++)
        {
            addHundredChip(player);
        }
        for (int i =0;i<twentyFives;i++)
        {
            addTwentyFiveChip(player);
        }
        for (int i =0;i<tens;i++)
        {
            addTenChip(player);
        }
        for (int i =0;i<fives;i++)
        {
            addFiveChip(player);
        }
        for (int i =0;i<ones;i++)
        {
            addOneChip(player);
        }
    }

    private Image setChip(Image temp)
    {
        temp.setSize(85,85);
        stage.addActor(temp);
        chipMoveBottom.play();
        temp.setUserObject(false);
        return temp;
    }



    private void addHundredChip(final Human player)
    {
        Image t = new Image(Chip.hundredDollarChip.image);
        t.setPosition((Gdx.graphics.getWidth()*1/128)+110,Gdx.graphics.getHeight()-220);
        int x  = player.getPlayerChip().hundredChipX;
        int y = player.getPlayerChip().hundredChipY;
        final Image temp = setChip(t);
        temp.setName("chip");
        temp.addAction(Actions.sequence(Actions.moveTo(x,y,0.3f)));
        temp.setOrigin(x,y);
        player.getPlayerChip().hundredChipY += 3;
        temp.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (temp.getUserObject().equals(false))
                {
                    boolean pieceAtBottom = false;
                    if (placedBets || (temp.getOriginY() != (player.getPlayerChip().hundredChipY - 3)))
                    {
                        return;
                    }
                    temp.setSize(70,70);
                    if (perfectPairActive)
                    {
                        if (player.getPlayerChip().perfectPairArray.get(4).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 280 : 445,(Gdx.graphics.getHeight()*3/4)+8,0.3f)));
                        player.getPlayerChip().perfectPairMoney += 100;
                        player.getPlayerChip().perfectPairArray.get(4).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().perfectPairArray);
                    }
                    else if (flushActive)
                    {
                        if (player.getPlayerChip().flushArray.get(4).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 610: 775,(Gdx.graphics.getHeight()*3/4)+103,0.3f)));
                        player.getPlayerChip().flushMoney += 100;
                        player.getPlayerChip().flushArray.get(4).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().flushArray);
                    }
                    else
                    {
                        if (player.getPlayerChip().betHeightHundred ==  (Gdx.graphics.getHeight()*1/4)+250)
                        {
                            pieceAtBottom = true;
                        }
                        player.getPlayerChip().betHeightHundred +=3;
                        temp.addAction(Actions.sequence(Actions.moveTo(player.getPlayerChip().betHundredPosX,player.getPlayerChip().betHeightHundred,0.3f)));
                        player.getPlayerChip().bettingMoney += 100;
                        betChipsHundred.add(temp);
                        fixChips(null);
                    }
                    player.removeBetMoney(100);
                    player.getPlayerChip().hundredChipY -= 3;
                    if (pieceAtBottom)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.setUserObject(true);
                }
                else
                {
                    if (placedBets )
                    {
                        return;
                    }
                    if (temp.getY() != (player.getPlayerChip().betHeightHundred) && temp.getY() != (Gdx.graphics.getHeight()*3/4)+8 && temp.getY() != (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        return;
                    }
                    temp.setSize(85,85);
                    if (temp.getY() == player.getPlayerChip().betHeightHundred)
                    {
                        player.getPlayerChip().betHeightHundred -=3;
                        player.getPlayerChip().bettingMoney -= 100;
                        betChipsHundred.remove(temp);
                    }
                    else if (temp.getY() == (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        player.getPlayerChip().flushArray.get(4).remove(temp);
                        player.getPlayerChip().flushMoney -= 100;
                    }
                    else
                    {
                        player.getPlayerChip().perfectPairArray.get(4).remove(temp);
                        player.getPlayerChip().perfectPairMoney -= 100;
                    }
                    temp.setName("chip");
                    if (player.getPlayerChip().hundredChipY == (Gdx.graphics.getHeight()*1/4)-100)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.addAction(Actions.sequence(Actions.moveTo(temp.getOriginX(),player.getPlayerChip().hundredChipY ,0.3f)));
                    temp.setOriginY(player.getPlayerChip().hundredChipY);
                    player.getPlayerChip().hundredChipY += 3;
                    temp.toFront();
                    player.addBetMoney(100);
                    temp.setUserObject(false);
                }

                displayBetTotal();
            };
        });
    }

    private void addTwentyFiveChip(final Human player)
    {
        Image t = new Image(Chip.twentyFiveDollarChip.image);
        t.setPosition(Gdx.graphics.getWidth()*1/128, Gdx.graphics.getHeight()-220);
        int x = player.getPlayerChip().twentyFiveChipX;
        int y = player.getPlayerChip().twentyFiveChipY;
        final Image temp = setChip(t);
        temp.setName("chip");
        temp.addAction(Actions.moveTo(x,y,0.3f));
        temp.setOrigin(x, y);
        player.getPlayerChip().twentyFiveChipY += 3;
        temp.setUserObject(false);
        temp.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (temp.getUserObject().equals(false))
                {
                    boolean pieceAtBottom = false;
                    if (placedBets || (temp.getOriginY() != (player.getPlayerChip().twentyFiveChipY - 3)))
                    {
                        return;
                    }
                    temp.setSize(70,70);
                    if (perfectPairActive)
                    {
                        if (player.getPlayerChip().perfectPairArray.get(3).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 300 : 465,(Gdx.graphics.getHeight()*3/4)+8,0.3f)));
                        player.getPlayerChip().perfectPairMoney += 25;
                        player.getPlayerChip().perfectPairArray.get(3).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().perfectPairArray);
                    }
                    else if (flushActive)
                    {
                        if (player.getPlayerChip().flushArray.get(3).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 630: 795,(Gdx.graphics.getHeight()*3/4)+103,0.3f)));
                        player.getPlayerChip().flushMoney += 25;
                        player.getPlayerChip().flushArray.get(3).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().flushArray);
                    }
                    else
                    {
                        if (player.getPlayerChip().betHeightTwentyFive ==  (Gdx.graphics.getHeight()*1/4)+240)
                        {
                            pieceAtBottom = true;
                        }
                        player.getPlayerChip().betHeightTwentyFive +=3;
                        temp.addAction(Actions.sequence(Actions.moveTo(player.getPlayerChip().betTwentyFivePosX,player.getPlayerChip().betHeightTwentyFive,0.3f)));
                        player.getPlayerChip().bettingMoney += 25;
                        betChipsTwentyFive.add(temp);
                        fixChips(null);
                    }
                    player.removeBetMoney(25);
                    player.getPlayerChip().twentyFiveChipY -= 3;
                    if (pieceAtBottom)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.setUserObject(true);
                }
                else
                {
                    if (placedBets )
                    {
                        return;
                    }
                    if (temp.getY() != (player.getPlayerChip().betHeightTwentyFive) && temp.getY() != (Gdx.graphics.getHeight()*3/4)+8 && temp.getY() != (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        return;
                    }
                    temp.setSize(85,85);
                    if (temp.getY() == player.getPlayerChip().betHeightTwentyFive)
                    {
                        player.getPlayerChip().betHeightTwentyFive -=3;
                        player.getPlayerChip().bettingMoney -= 25;
                        betChipsTwentyFive.remove(temp);
                    }
                    else if (temp.getY() == (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        player.getPlayerChip().flushArray.get(3).remove(temp);
                        player.getPlayerChip().flushMoney -= 25;
                    }
                    else
                    {
                        player.getPlayerChip().perfectPairArray.get(3).remove(temp);
                        player.getPlayerChip().perfectPairMoney -= 25;
                    }
                    temp.setName("chip");
                    if (player.getPlayerChip().twentyFiveChipY == (Gdx.graphics.getHeight()*1/4)-90)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.addAction(Actions.sequence(Actions.moveTo(temp.getOriginX(),player.getPlayerChip().twentyFiveChipY ,0.3f)));
                    temp.setOriginY(player.getPlayerChip().twentyFiveChipY);
                    player.getPlayerChip().twentyFiveChipY += 3;
                    temp.toFront();
                    player.addBetMoney(25);
                    temp.setUserObject(false);
                }
                displayBetTotal();
            };
        });
    }

    private void addTenChip(final Human player)
    {
        Image t = new Image(Chip.tenDollarChip.image);
        t.setPosition((Gdx.graphics.getWidth()*1/128)+220, Gdx.graphics.getHeight()-110);
        int x  = player.getPlayerChip().tenChipX;
        int y = player.getPlayerChip().tenChipY;
        final Image temp = setChip(t);
        temp.setName("chip");
        temp.addAction(Actions.sequence(Actions.moveTo(x,y,0.3f)));
        temp.setOrigin(x,y);
        player.getPlayerChip().tenChipY += 3;
        temp.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (temp.getUserObject().equals(false))
                {
                    boolean pieceAtBottom = false;
                    if (placedBets || (temp.getOriginY() != (player.getPlayerChip().tenChipY - 3)))
                    {
                        return;
                    }
                    temp.setSize(70,70);
                    if (perfectPairActive)
                    {
                        if (player.getPlayerChip().perfectPairArray.get(2).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 320 : 485,(Gdx.graphics.getHeight()*3/4)+8,0.3f)));
                        player.getPlayerChip().perfectPairMoney += 10;
                        player.getPlayerChip().perfectPairArray.get(2).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().perfectPairArray);
                    }
                    else if (flushActive)
                    {
                        if (player.getPlayerChip().flushArray.get(2).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ?650: 815,(Gdx.graphics.getHeight()*3/4)+103,0.3f)));
                        player.getPlayerChip().flushMoney += 10;
                        player.getPlayerChip().flushArray.get(2).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().flushArray);
                    }
                    else
                    {
                        if (player.getPlayerChip().betHeightTen ==  (Gdx.graphics.getHeight()*1/4)+230)
                        {
                            pieceAtBottom = true;
                        }
                        player.getPlayerChip().betHeightTen +=3;
                        temp.addAction(Actions.sequence(Actions.moveTo(player.getPlayerChip().betTenPosX,player.getPlayerChip().betHeightTen,0.3f)));
                        player.getPlayerChip().bettingMoney += 10;
                        betChipsTen.add(temp);
                        fixChips(null);
                    }
                    player.removeBetMoney(10);
                    player.getPlayerChip().tenChipY -= 3;
                    if (pieceAtBottom)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.setUserObject(true);
                }
                else
                {
                    if (placedBets )
                    {
                        return;
                    }
                    if (temp.getY() != (player.getPlayerChip().betHeightTen) && temp.getY() != (Gdx.graphics.getHeight()*3/4)+8 && temp.getY() != (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        return;
                    }
                    temp.setSize(85,85);
                    if (temp.getY() == player.getPlayerChip().betHeightTen)
                    {
                        player.getPlayerChip().betHeightTen -=3;
                        player.getPlayerChip().bettingMoney -= 10;
                        betChipsTen.remove(temp);
                    }
                    else if (temp.getY() == (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        player.getPlayerChip().flushArray.get(2).remove(temp);
                        player.getPlayerChip().flushMoney -= 10;
                    }
                    else
                    {
                        player.getPlayerChip().perfectPairArray.get(2).remove(temp);
                        player.getPlayerChip().perfectPairMoney -= 10;
                    }
                    temp.setName("chip");
                    if (player.getPlayerChip().tenChipY == (Gdx.graphics.getHeight()*1/4)+70)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.addAction(Actions.sequence(Actions.moveTo(temp.getOriginX(),player.getPlayerChip().tenChipY ,0.3f)));
                    temp.setOriginY(player.getPlayerChip().tenChipY);
                    player.getPlayerChip().tenChipY += 3;
                    temp.toFront();
                    player.addBetMoney(10);
                    temp.setUserObject(false);
                }
                displayBetTotal();
            };
        });
    }


    private void addFiveChip(final  Human player)
    {
        Image t = new Image(Chip.fiveDollarChip.image);
        t.setPosition((Gdx.graphics.getWidth()*1/128)+110, Gdx.graphics.getHeight()-110);
        int x  = player.getPlayerChip().fiveChipX;
        int y = player.getPlayerChip().fiveChipY;
        final Image temp = setChip(t);
        temp.setName("chip");
        temp.addAction(Actions.sequence(Actions.moveTo(x,y,0.3f)));
        temp.setOrigin(x,y);
        player.getPlayerChip().fiveChipY += 3;
        temp.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (temp.getUserObject().equals(false))
                {
                    boolean pieceAtBottom = false;
                    if (placedBets || (temp.getOriginY() != (player.getPlayerChip().fiveChipY - 3)))
                    {
                        return;
                    }
                    temp.setSize(70,70);
                    if (perfectPairActive)
                    {
                        if (player.getPlayerChip().perfectPairArray.get(1).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 340 : 505,(Gdx.graphics.getHeight()*3/4)+8,0.3f)));
                        player.getPlayerChip().perfectPairMoney += 5;
                        player.getPlayerChip().perfectPairArray.get(1).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().perfectPairArray);
                    }
                    else if (flushActive)
                    {
                        if (player.getPlayerChip().flushArray.get(1).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ?670: 835,(Gdx.graphics.getHeight()*3/4)+103,0.3f)));
                        player.getPlayerChip().flushMoney += 5;
                        player.getPlayerChip().flushArray.get(1).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().flushArray);
                    }
                    else
                    {
                        if (player.getPlayerChip().betHeightFive ==  (Gdx.graphics.getHeight()*1/4)+220)
                        {
                            pieceAtBottom = true;
                        }
                        player.getPlayerChip().betHeightFive +=3;
                        temp.addAction(Actions.sequence(Actions.moveTo(player.getPlayerChip().betFivePosX,player.getPlayerChip().betHeightFive,0.3f)));
                        player.getPlayerChip().bettingMoney += 5;
                        betChipsFive.add(temp);
                        fixChips(null);
                    }
                    player.removeBetMoney(5);
                    player.getPlayerChip().fiveChipY -= 3;
                    if (pieceAtBottom)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.setUserObject(true);
                }
                else
                {
                    if (placedBets )
                    {
                        return;
                    }
                    if (temp.getY() != (player.getPlayerChip().betHeightFive) && temp.getY() != (Gdx.graphics.getHeight()*3/4)+8 && temp.getY() != (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        return;
                    }
                    temp.setSize(85,85);
                    if (temp.getY() == player.getPlayerChip().betHeightFive)
                    {
                        player.getPlayerChip().betHeightFive -=3;
                        player.getPlayerChip().bettingMoney -= 5;
                        betChipsFive.remove(temp);
                    }
                    else if (temp.getY() == (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        player.getPlayerChip().flushArray.get(1).remove(temp);
                        player.getPlayerChip().flushMoney -= 5;
                    }
                    else
                    {
                        player.getPlayerChip().perfectPairArray.get(1).remove(temp);
                        player.getPlayerChip().perfectPairMoney -= 5;
                    }
                    temp.setName("chip");
                    if (player.getPlayerChip().fiveChipY == (Gdx.graphics.getHeight()*1/4)+60)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.addAction(Actions.sequence(Actions.moveTo(temp.getOriginX(),player.getPlayerChip().fiveChipY ,0.3f)));
                    temp.setOriginY(player.getPlayerChip().fiveChipY);
                    player.getPlayerChip().fiveChipY += 3;
                    temp.toFront();
                    player.addBetMoney(5);
                    temp.setUserObject(false);
                }
                displayBetTotal();
            };
        });
    }

    private void addOneChip(final Human player)
    {
        Image t = new Image(Chip.oneDollarChip.image);
        t.setPosition(Gdx.graphics.getWidth()*1/128, Gdx.graphics.getHeight()-110);
        int x  = player.getPlayerChip().oneChipX;
        int y = player.getPlayerChip().oneChipY;
        final Image temp = setChip(t);
        temp.setName("chip");
        temp.addAction(Actions.sequence(Actions.moveTo(x,y,0.3f)));
        temp.setOrigin(x,y);
        player.getPlayerChip().oneChipY += 3;
        temp.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (temp.getUserObject().equals(false))
                {
                    boolean pieceAtBottom = false;
                    if (placedBets || (temp.getOriginY() != (player.getPlayerChip().oneChipY - 3)))
                    {
                        return;
                    }
                    temp.setSize(70,70);
                    if (perfectPairActive)
                    {
                        if (player.getPlayerChip().perfectPairArray.get(0).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ? 360 : 525,(Gdx.graphics.getHeight()*3/4)+8,0.3f)));
                        player.getPlayerChip().perfectPairMoney += 1;
                        player.getPlayerChip().perfectPairArray.get(0).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().perfectPairArray);
                    }
                    else if (flushActive)
                    {
                        if (player.getPlayerChip().flushArray.get(0).size() == 0)
                        {
                            pieceAtBottom = true;
                        }
                        temp.addAction(Actions.sequence(Actions.moveTo(player == playerOne ?670: 835,(Gdx.graphics.getHeight()*3/4)+103,0.3f)));
                        player.getPlayerChip().flushMoney += 1;
                        player.getPlayerChip().flushArray.get(0).add(temp);
                        temp.toFront();
                        fixChips(player.getPlayerChip().flushArray);
                    }
                    else
                    {
                        if (player.getPlayerChip().betHeightOne ==  (Gdx.graphics.getHeight()*1/4)+210)
                        {
                            pieceAtBottom = true;
                        }
                        player.getPlayerChip().betHeightOne +=3;
                        temp.addAction(Actions.sequence(Actions.moveTo(player.getPlayerChip().betOnePosX,player.getPlayerChip().betHeightOne,0.3f)));
                        player.getPlayerChip().bettingMoney += 1;
                        betChipsOne.add(temp);
                        fixChips(null);
                    }
                    player.removeBetMoney(1);
                    player.getPlayerChip().oneChipY -= 3;
                    if (pieceAtBottom)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.setUserObject(true);
                }
                else
                {
                    if (placedBets )
                    {
                        return;
                    }
                    if (temp.getY() != (player.getPlayerChip().betHeightOne) && temp.getY() != (Gdx.graphics.getHeight()*3/4)+8 && temp.getY() != (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        return;
                    }
                    temp.setSize(85,85);
                    if (temp.getY() == player.getPlayerChip().betHeightOne)
                    {
                        player.getPlayerChip().betHeightOne -=3;
                        player.getPlayerChip().bettingMoney -= 1;
                        betChipsOne.remove(temp);
                    }
                    else if (temp.getY() == (Gdx.graphics.getHeight()*3/4)+103)
                    {
                        player.getPlayerChip().flushArray.get(0).remove(temp);
                        player.getPlayerChip().flushMoney -= 1;
                    }
                    else
                    {
                        player.getPlayerChip().perfectPairArray.get(0).remove(temp);
                        player.getPlayerChip().perfectPairMoney -= 1;
                    }
                    temp.setName("chip");
                    if (player.getPlayerChip().oneChipY == (Gdx.graphics.getHeight()*1/4)+50)
                    {
                        chipMoveBottom.play();
                    }
                    else
                    {
                        chipMove.play();
                    }
                    temp.addAction(Actions.sequence(Actions.moveTo(temp.getOriginX(),player.getPlayerChip().oneChipY ,0.3f)));
                    temp.setOriginY(player.getPlayerChip().oneChipY);
                    player.getPlayerChip().oneChipY += 3;
                    temp.toFront();
                    player.addBetMoney(1);
                    temp.setUserObject(false);
                }
                displayBetTotal();
            };
        });
    }

    private void fixChips(ArrayList<ArrayList<Image>> arrayList)
    {
        ArrayList<Image> chipsHundredArray = betChipsHundred;
        ArrayList<Image> chipsTwentyFiveArray = betChipsTwentyFive;
        ArrayList<Image> chipsTenArray = betChipsTen;
        ArrayList<Image> chipsFiveArray = betChipsFive;
        ArrayList<Image> chipsOneArray = betChipsOne;

        if (arrayList != null)
        {
            chipsHundredArray = arrayList.get(4);
            chipsTwentyFiveArray = arrayList.get(3);
            chipsTenArray = arrayList.get(2);
            chipsFiveArray = arrayList.get(1);
            chipsOneArray = arrayList.get(0);
        }

        for (int i = 0;i<chipsOneArray.size();i++)
        {
            ((Image)chipsOneArray.get(i)).toFront();
        }
        for (int i = 0;i<chipsFiveArray.size();i++)
        {
            ((Image)chipsFiveArray.get(i)).toFront();
        }
        for (int i = 0;i<chipsTenArray.size();i++)
        {
            ((Image)chipsTenArray.get(i)).toFront();
        }
        for (int i = 0;i<chipsTwentyFiveArray.size();i++)
        {
            ((Image)chipsTwentyFiveArray.get(i)).toFront();
        }
        for (int i = 0;i<chipsHundredArray.size();i++)
        {
            ((Image)chipsHundredArray.get(i)).toFront();
        }
    }

    private void readPreviousScenarios()
    {
        try {

            previousScenarios = json.fromJson(Logger[][][].class, prefs.getString("test"));
            if (previousScenarios == null)
            {
                previousScenarios = new Logger[22][22][22];
            }

        }

        catch (Exception e)
        {
            previousScenarios = new Logger[22][22][22];
        }

    }

    private void saveScenarios(boolean win)
    {
        for (int i = 0;i< tempMoves.size;i++)
        {
            String[] line = ((String) (tempMoves.get(i))).split(" ");
            int playerTotal = Integer.parseInt(line[0]);
            int player2Total = Integer.parseInt(line[1]);
            int dealerTotal = Integer.parseInt(line[2]);
            boolean status = Boolean.parseBoolean(line[3]);
            Logger log = previousScenarios[playerTotal][player2Total][dealerTotal];
            if (log == null)
            {
                log = new Logger();
            }
            log.addResult(status,win);
        }
        try {
            hashTable = new Hashtable<String, String>();
            hashTable.put("test", json.toJson(previousScenarios) );
            prefs.put(hashTable);
            prefs.flush();
        }

        catch (Exception e)
        {

        }
    }

    private void addScenario(boolean status)
    {
        Logger log = new Logger();
        int playerOneTotal = playerOne.getTotal() > 21 ? 0 : playerOne.getTotal();
        int playerTwoTotal = playerTwo.getTotal() > 21 ? 0 : playerTwo.getTotal();
        int dealerTotal = dealer.getTotal() > 21 ? 0 : dealer.getTotal();
        if (previousScenarios[playerOneTotal][playerTwoTotal][dealerTotal] != null)
        {
            log = previousScenarios[playerOneTotal][playerTwoTotal][dealerTotal];
        }
        log.addMove(status);
        previousScenarios[playerOneTotal][playerTwoTotal][dealerTotal] = log;
        tempMoves.add(playerOneTotal + " " + playerTwoTotal + " " + dealerTotal + " " + status);
    }

    private void insuranceOutCome()
    {
        if (dealer.getTotal() ==21 || (dealerHadAce && dealer.getTotal()==11))
        {
            hitButton.remove();
            doubleButton.remove();
            standButton.remove();
            insuranceSortChips(playerInsurance, playerOne);
            insuranceSortChips(player2Insurance, playerTwo);
            returnChipsToBank(null);
            String text = "";
            if (playerInsurance)
            {
                text += playerOne.getName() + " has taken out insurance so there bet will be returned \n";
            }
            if (player2Insurance)
            {
                text += playerTwo.getName() + " has taken out insurance so there bet will be returned";
            }
            if (text.equals(""))
            {
                text += "You have both lost as you both didn't take out insurance";
            }
            endOfRoundDialog = new Dialog("End of Round", BlackJack.mySkin);
            final Label heading = new Label(text, BlackJack.mySkin, "title-plain");
            endOfRoundDialog.text(heading);
            endOfRoundDialog.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
            endOfRoundDialog.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    endOfRoundDialog.remove();
                    resetLayout();
                }
            });
            endOfRoundDialog.show(stage);
            endOfRoundDialog.setMovable(false);



        }
        else
        {
            returnChipsToBank(playerOne.getPlayerChip().insuranceArray);
            returnChipsToBank(playerTwo.getPlayerChip().insuranceArray);
            playerOneInsuranceLabel.setText("");
            playerTwoInsuranceLabel.setText("");
            return;
        }
    }

    private void resetLayout()
    {
        if (playerOne.getBetMoney() <= 0 || playerOne.getBetMoney() <= 0)
        {
            final Dialog roundDialog = new Dialog("You Lose", BlackJack.mySkin);
            final Label heading = new Label("you have run out of chips, you will be returned to the main menu", BlackJack.mySkin, "title-plain");
            roundDialog.text(heading);
            roundDialog.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
            roundDialog.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new MenuScreen(game));
                }});
            roundDialog.show(stage);
            roundDialog.setMovable(false);
            return;
        }
        hitButton.remove();
        doubleButton.remove();
        standButton.remove();
        betChipsHundred.clear();
        playerOneCardTotal.remove();
        playerTwoCardTotal.remove();
        playerStand = false;
        betChipsOne.clear();
        betChipsFive.clear();
        betChipsTen.clear();
        betChipsTwentyFive.clear();
        playerOne.reset();
        playerTwo.reset();
        cardSet = new CardSet();
        playerOneHasDouble = false;
        playerTwoHasDouble = false;
        dealer.reset();
        stage.addActor(placeBetsButton);
        stage.addActor(allInButtonPlayerOne);
        stage.addActor(allInButtonPlayerTwo);
        stage.addActor(repeatButtonPlayerOne);
        stage.addActor(repeatButtonPlayerTwo);
        placedBets = false;
        playerOne.getPlayerChip().reset();
        playerTwo.getPlayerChip().reset();
        playerOneOut = false;
        playerTwoOut = false;
        playerInsurance = false;
        player2Insurance = false;
        playerOneInsuranceLabel.setText("");
        playerTwoInsuranceLabel.setText("");
        displayBetTotal();
        clearCards(stage.getActors());
        playerNameLabel.setColor(Color.RED);
        player2NameLabel.setColor(Color.WHITE);
        playerOneBetReady = false;
        playerTwoBetReady = false;
        if (tutorialMode) {
            bettingDialog.show(stage);
        }
    }

    private void insuranceSortChips(boolean insurance, Human player)
    {
        if (insurance)
        {
            returnChipsToPlayer(player,player.getPlayerChip().insuranceArray);
            returnChipsToPlayer(player, null);
            player.addBetMoney(player.getPlayerChip().bettingMoney + player.getPlayerChip().bettingMoney/2);
            displayBetTotal();
        }
        else
        {
            returnChipsToBank(player.getPlayerChip().insuranceArray);
        }
    }

    private Image newCardImage(Texture texture)
    {
        Image temp = new Image(texture);
        temp.setSize(120,150);
        stage.addActor(temp);
        return temp;
    }
}