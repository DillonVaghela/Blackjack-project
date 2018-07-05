package com.mygdx.game.Domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.BlackJack;

import javax.xml.soap.Text;

public class MediaLoader {

    public static Music background, cardMove, cardFlip, chipMoveBottom, chipMove;
    private static MediaLoader instance;
    private static TextButton backgroundMusic, soundEffects;
    private static boolean startGame;

    public static void create()
    {
        startGame = false;
        cardFlip =  Gdx.audio.newMusic(Gdx.files.internal("sounds/cardFlip.mp3"));
        cardFlip.setVolume(0.5f);
        cardMove =  Gdx.audio.newMusic(Gdx.files.internal("sounds/cardmove.mp3"));
        cardMove.setVolume(0.5f);
        chipMove = Gdx.audio.newMusic(Gdx.files.internal("sounds/chiptop.wav"));
        chipMove.setVolume(0.5f);
        chipMoveBottom = Gdx.audio.newMusic(Gdx.files.internal("sounds/chipbottom.wav"));
        chipMoveBottom.setVolume(1f);
        background = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));
        background.setLooping(true);
        background.setVolume(0.25f);
        backgroundMusic = new TextButton("Background music", BlackJack.mySkin, "toggle");
        backgroundMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (background.isPlaying())
                {
                    background.pause();
                }
                else
                {
                    background.play();
                }
            }
        });
        backgroundMusic.setPosition(Gdx.graphics.getWidth()-160, 40);
        backgroundMusic.setChecked(true);
        soundEffects = new TextButton("Sound effects", BlackJack.mySkin, "toggle");
        soundEffects.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cardMove.getVolume()>0)
                {
                    cardMove.setVolume(0);
                    cardFlip.setVolume(0);
                    chipMoveBottom.setVolume(0);
                    chipMove.setVolume(0);
                }
                else
                {
                    cardMove.setVolume(0.5f);
                    cardFlip.setVolume(0.5f);
                    chipMoveBottom.setVolume(0.5f);
                    chipMove.setVolume(0.5f);
                }
            }
        });
        soundEffects.setPosition(Gdx.graphics.getWidth()-160+ (backgroundMusic.getWidth() - soundEffects.getWidth()), 5);
        soundEffects.setChecked(true);
    }
    public static void dispose()
    {
        background.dispose();
    }

    public static MediaLoader getInstance()
    {
        if (instance == null)
        {
            instance = new MediaLoader();
            MediaLoader.create();
        }
        return instance;
    }

    public TextButton getBackgroundMusic()
    {
        return backgroundMusic;
    }

    public TextButton getSoundEffects()
    {
        return soundEffects;
    }

    public void backgroundMusicPlay()
    {
        if (!startGame)
        {
            background.play();
            startGame = true;
        }
    }
}
