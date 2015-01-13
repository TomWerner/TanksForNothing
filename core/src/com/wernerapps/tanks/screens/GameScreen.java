package com.wernerapps.tanks.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.TanksGame;

public class GameScreen extends AbstractScreen
{

    public GameScreen(TanksGame game, int targetLevelId)
    {
        super(game, new ScalingViewport(Scaling.none, game.getWidth(), game.getHeight()));
        stage = new GameWorld(game, targetLevelId);

        // play the level music
//        game.getMusicManager().play(new BackgroundMusic("music/ObservingTheStar.ogg", "Level Music"));
    }

    @Override
    protected boolean isGameScreen()
    {
        return true;
    }

    @Override
    public void show()
    {
        super.show();
        
        // add a fade-in effect to the whole stage
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction(Actions.fadeIn(0.5f));
    }

    @Override
    public void pause()
    {
        ((GameWorld) stage).pause();
    }
}
