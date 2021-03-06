package com.wernerapps.tanks.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.wernerapps.tanks.game.TanksGame;
import com.wernerapps.tanks.levelcreator.LevelCreatorStage;

public class LevelCreatorScreen extends AbstractScreen
{

    public LevelCreatorScreen(TanksGame game)
    {
        super(game, new ScalingViewport(Scaling.none, game.getWidth(), game.getHeight()));
        stage = new LevelCreatorStage(game);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void show()
    {
        super.show();

        // add a fade-in effect to the whole stage
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction(Actions.fadeIn(0.5f));
    }
}
