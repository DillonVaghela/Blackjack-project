package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jcraft.jorbis.DspState;
import com.mygdx.game.BlackJack;
import org.lwjgl.opengl.Display;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width= 1070;
		config.height= 778;
		new LwjglApplication(new BlackJack(), config)
		{
			@Override
			public void exit()
			{

				((BlackJack)getApplicationListener()).canClose();

			}
		};




	}
}


