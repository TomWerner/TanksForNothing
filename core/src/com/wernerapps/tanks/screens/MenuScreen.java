package com.wernerapps.tanks.screens;

import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.menu.MenuStage;

public class MenuScreen extends AbstractScreen
{
    public MenuScreen(TanksGame game)
    {
        super(game, new ScalingViewport(Scaling.none, game.getWidth(), game.getHeight()));
        stage = new MenuStage(game);
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void pause()
    {

    }
    
    
}