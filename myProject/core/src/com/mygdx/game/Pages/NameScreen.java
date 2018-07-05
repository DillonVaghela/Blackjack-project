package com.mygdx.game.Pages;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.BlackJack;
import com.mygdx.game.Domain.MediaLoader;

public class NameScreen implements Screen
{
    private static Stage stage;
    private Game game;
    private Batch batch;
    private Texture background;
    private Dialog Conformation;
    private static String nameOne, nameTwo;
    private static Boolean tutorialMode;
    private TextField playerTwoName;
    private  Dialog enterNames;
    private static MediaLoader mediaLoader = MediaLoader.getInstance();
    private static Screen screen;


    public NameScreen(Game aGame) {
        this.game = aGame;
        screen = this;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        tutorialMode = false;

        background = new Texture("table.png");
        String message ="";
        if (MenuScreen.getIsSinglePlayer())
        {
            message += "name";
        }
        else
        {
            message += "names";
        }
        Label heading = new Label("enter player " +message, BlackJack.mySkin, "title");
        heading.setAlignment(Align.center);
        heading.setFontScale(1);
        heading.setY(Gdx.graphics.getHeight()*8/10);
        heading.setWidth(Gdx.graphics.getWidth());
        stage.addActor(heading);

        stage.addActor(mediaLoader.getBackgroundMusic());
        stage.addActor(mediaLoader.getSoundEffects());

        TextButton tutorialModeButton = new TextButton("Tutorial Mode",BlackJack.mySkin, "toggle");
        tutorialModeButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (tutorialMode)
                {
                    tutorialMode = false;
                }
                else
                {
                    tutorialMode = true;
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
        tutorialModeButton.setWidth(Gdx.graphics.getWidth()/8);
        tutorialModeButton.setPosition(Gdx.graphics.getWidth()/2-tutorialModeButton.getWidth()/2,(Gdx.graphics.getHeight()/2-tutorialModeButton.getHeight()/2));
        stage.addActor(tutorialModeButton);

        TextButton backButton = new TextButton("Main Menu",BlackJack.mySkin);
        backButton.setWidth(Gdx.graphics.getWidth()/8);
        backButton.setPosition(20,10);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

        Button playButton = new TextButton("Play",BlackJack.mySkin);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Conformation.show(stage);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        playButton.setWidth(Gdx.graphics.getWidth()/8);
        playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2,(Gdx.graphics.getHeight()/2-playButton.getHeight()/2)-35);
        stage.addActor(playButton);

        TextField.TextFieldFilter filter = new TextField.TextFieldFilter() {
            public boolean acceptChar(TextField textField, char c) {
                if (Character.toString(c).matches("^[a-zA-Z]")) {
                    return true;
                }
                return false;
            }
        };

        final TextField playerOneName = createTextField("", 100);
        playerOneName.setTextFieldFilter(filter);


        if (!(MenuScreen.getIsSinglePlayer()))
        {
            playerTwoName = createTextField("", 50);
            playerTwoName.setTextFieldFilter(filter);

        }



        Conformation = new Dialog("Warning", BlackJack.mySkin, "dialog") {
            public void result(Object obj) {
                if (obj.equals(true))
                {
                    if (MenuScreen.getIsSinglePlayer())
                    {
                        if (!(playerOneName.getText().equals("")))
                        {
                            nameOne = playerOneName.getText();
                            game.setScreen(new SinglePlayerScreen(game));
                        }
                        else
                        {
                            errorMessage("enter player name");
                        }
                    }
                    else
                    {

                        if (!(playerOneName.getText().equals("")) && !(playerTwoName.getText().equals("")))
                        {
                            nameOne = playerOneName.getText();
                            nameTwo = playerTwoName.getText();
                            game.setScreen(new MultiPlayerScreen(game));
                        }
                        else
                        {
                            errorMessage("enter both player names");
                        }
                    }
                }
            }
        };
        Conformation.text("Are you sure you want to go to confirm names, you cannot return to this screen without resetting the names?");
        Conformation.button("Yes", true);
        Conformation.button("No", false);
        Conformation.key(Input.Keys.ENTER, true);
        Conformation.key(Input.Keys.ESCAPE, false);


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

    public static Stage getStage()
    {
        return stage;
    }

    private TextField createTextField(String name, int x)
    {
        TextField textField = new TextField(name, BlackJack.mySkin);
        textField.setWidth(Gdx.graphics.getWidth() / 4);
        textField.setHeight(Gdx.graphics.getHeight() / 25);
        textField.setPosition(Gdx.graphics.getWidth() / 2 - textField.getWidth() / 2, (Gdx.graphics.getHeight() / 2 - textField.getHeight() / 2) + x);
        stage.addActor(textField);
        return textField;
    }

    public static String getNameOne()
    {
        return nameOne;
    }

    public static String getNameTwo()
    {
        return nameTwo;
    }

    public static boolean getIsTutorialMode()
    {
        if (tutorialMode == null)
            return false;
        return tutorialMode;
    }

    public void errorMessage(String message)
    {
        enterNames = new Dialog("ERROR", BlackJack.mySkin);
        enterNames.setY(Gdx.graphics.getHeight()*1/2);
        enterNames.setWidth(Gdx.graphics.getWidth());
        Label heading = new Label(message, BlackJack.mySkin, "title-plain");
        enterNames.text(heading);
        enterNames.show(stage);
        enterNames.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                enterNames.hide();
            };
        });

    }
}