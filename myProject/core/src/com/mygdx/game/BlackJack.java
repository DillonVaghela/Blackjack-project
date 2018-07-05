package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.mygdx.game.Pages.MenuScreen;
import com.mygdx.game.Pages.MultiPlayerScreen;
import com.mygdx.game.Pages.NameScreen;
import com.mygdx.game.Pages.SinglePlayerScreen;
import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

import javax.xml.soap.Text;
import java.io.Console;

public class BlackJack extends Game {

	public static Skin mySkin;
	//public static GDXDialogs gdxDialogs;

	@Override
	public void create ()
	{

		//System.out.println(Gdx.files.);
		mySkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		VisUI.load();
		//gdxDialogs = GDXDialogsSystem.install();
		this.setScreen(new MenuScreen(this));
		Gdx.graphics.setResizable(false);



	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {

	}

	public void canClose()
	{
		if (MenuScreen.getScreen() == this.getScreen() )
		{
			closeScreen(MenuScreen.getStage());
		}

		else if (NameScreen.getScreen() == this.getScreen() )
		{
			closeScreen(NameScreen.getStage());
		}
		else if (SinglePlayerScreen.getScreen() == this.getScreen() )
		{

			closeScreenSave(SinglePlayerScreen.getStage(), "SinglePlayer");
		}
		else
		{
			closeScreenSave(MultiPlayerScreen.getStage(),"MultiPlayer");
		}
	}

	public static void closeScreen(Stage stage)
	{
		Dialog closeApplication = new Dialog("Warning", BlackJack.mySkin, "dialog") {
			public void result(Object obj) {
				if (obj.equals(true))
				{
					closeApp();
				}
			}
		};
		closeApplication.text("Are you sure you want to close application?");
		closeApplication.button("Yes", true); //sends "true" as the result
		closeApplication.button("No", false);  //sends "false" as the result
		closeApplication.button("Cancel", false);  //sends "false" as the result
		closeApplication.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
		closeApplication.key(Input.Keys.ESCAPE, false);
		closeApplication.show(stage);


	}

	public static void closeScreenSave(Stage stage, final String screen)
	{
		Dialog closeApplication = new Dialog("Warning", BlackJack.mySkin, "dialog") {
			public void result(Object obj) {
				if (obj.equals(1))
				{
					closeApp();
				}
				else if (obj.equals(0))
				{
					if (screen.equals("SinglePlayer"))
					{
						SinglePlayerScreen.saveGame(true);
					}
					else
					{
						MultiPlayerScreen.saveGame(true);
					}

				}
			}
		};
		closeApplication.text("Do you want to save the application before closing?");
		closeApplication.button("Save and Exit", 0); //sends "true" as the result
		closeApplication.button("Exit", 1);  //sends "false" as the result
		closeApplication.button("Cancel", 2);  //sends "false" as the result
		closeApplication.key(Input.Keys.ENTER, 0); //sends "true" when the ENTER key is pressed
		closeApplication.key(Input.Keys.ESCAPE, 2);
		closeApplication.show(stage);

	}

	private static void closeApp()
	{
		System.exit(0);
	}


}
