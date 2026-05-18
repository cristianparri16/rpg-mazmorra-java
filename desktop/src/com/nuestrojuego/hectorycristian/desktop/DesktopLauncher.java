package com.nuestrojuego.hectorycristian.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;
import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;
import com.nuestrojuego.hectorycristian.MyMini2DxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(MyMini2DxGame.GAME_IDENTIFIER);
		config.width = 1920;
		config.height = 1080;
		config.vSyncEnabled = true;
		config.fullscreen = false;

		new DesktopMini2DxGame(new MyMini2DxGame(), config);
	}
}