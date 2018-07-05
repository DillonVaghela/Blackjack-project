package com.mygdx.game.Pages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;
import com.mygdx.game.BlackJack;
import com.mygdx.game.Domain.Human;
import com.mygdx.game.Domain.Logger;
import com.mygdx.game.Domain.MediaLoader;
import com.mygdx.game.Domain.Player;

import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class MenuScreen implements Screen {

    private static Stage stage;
    private static Game game;
    private Batch batch;
    private static MenuScreen instance;
    private Texture background;
    private static boolean isSinglePlayer;
    private static MediaLoader mediaLoader = MediaLoader.getInstance();
    private static FileChooser fileChooser;
    private static Player[] savedFile;
    private static Screen screen;
    private static Array gameData = new Array();

    public  MenuScreen (Game agame)
    {
        screen = this;
        this.game = agame;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        mediaLoader.backgroundMusicPlay();
        stage.addActor(mediaLoader.getBackgroundMusic());
        stage.addActor(mediaLoader.getSoundEffects());


        background = new Texture("table.png");
       //background

        Label heading = new Label("Welcome to BlackJack", BlackJack.mySkin, "title");
        heading.setAlignment(Align.center);
        heading.setFontScale(1f);
        heading.setY(Gdx.graphics.getHeight()*9/10);
        heading.setWidth(Gdx.graphics.getWidth());
        stage.addActor(heading);


        FileChooser.setDefaultPrefsName("com.DillonVaghela.BlackJack.filechooser");
        fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        FileTypeFilter typeFilter = new FileTypeFilter(false);
        typeFilter.addRule("Text files (*.txt)", "txt");
        fileChooser.setFileTypeFilter(typeFilter);
        fileChooser.clearListeners();
        fileChooser.setListener(new SingleFileChooserListener() {
            @Override
            protected void selected(FileHandle fileHandle) {
                fileChooser.fadeOut(1f);
                String filePath = fileHandle.file().getAbsolutePath();
                loadGame(filePath);
            }
        });

        Button loadGameButton = setButton("Load game", 150);
        loadGameButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (fileChooser.getMode() != FileChooser.Mode.OPEN)
                {
                    fileChooser.setMode(FileChooser.Mode.OPEN);
                }
                stage.addActor(fileChooser.fadeIn(1f));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Button singlePlayerButton = setButton("Single Player", 100);
        singlePlayerButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                isSinglePlayer = true;
                savedFile = null;
                game.setScreen(new NameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        Button twoPlayerButton = setButton( "Two Player", 50);
        twoPlayerButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                isSinglePlayer = false;
                savedFile = null;
                game.setScreen(new NameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });



    }
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta)
    {

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        dispose();
    }

    @Override
    public void dispose()
    {

        batch.dispose();
        stage.dispose();
    }

    public static Screen getScreen()
    {
        return screen;
    }


    public static boolean getIsSinglePlayer()
    {
        return isSinglePlayer;
    }



    private Button setButton( String text, int n)
    {
        TextButton button = new TextButton(text,BlackJack.mySkin);
        button.setWidth(Gdx.graphics.getWidth()/4);
        button.setPosition(Gdx.graphics.getWidth()/2-button.getWidth()/2,(Gdx.graphics.getHeight()/2-button.getHeight()/2)+n);
        stage.addActor(button);
        return button;
    }

    public static Array getGameData()
    {
        gameData = new Array();
        gameData.add(fileChooser);
        if (savedFile != null) {
            gameData.add(savedFile);
        }
        return gameData;
    }

    private void loadGame(String filePath)
    {
        try {
            String fileName = filePath;
            FileInputStream fin= new FileInputStream (fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            savedFile = (Human[]) ois.readObject();
            fin.close();
            ois.close();

            if (savedFile.length == 1)
            {
                isSinglePlayer = true;
                game.setScreen(new SinglePlayerScreen(game));
            }
            else
            {
                isSinglePlayer = false;
                game.setScreen(new MultiPlayerScreen(game));
            }

        }

        catch (Exception e)
        {
            e.printStackTrace();
            final Dialog errorDialog = new Dialog("Error", BlackJack.mySkin);
            final Label heading = new Label("File not Valid", BlackJack.mySkin, "title-plain");
            errorDialog.text(heading);
            errorDialog.setPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
            errorDialog.setMovable(false);
            errorDialog.addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    errorDialog.hide();
                };
            });
            errorDialog.show(stage);
        }

    }



    public static Stage getStage()
    {
        return stage;
    }




}
