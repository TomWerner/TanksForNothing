package com.wernerapps.tanks.states;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;
import com.wernerapps.tanks.gameobjects.NextLevelUI;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Tank;

public class LevelDoneState extends StateCondition
{
    private static float fadeDelay = 3;

    public LevelDoneState(final String message)
    {
        super(GameState.LEVEL_DONE, new Condition()
        {

            @Override
            public boolean isDone(GameWorld world, float delta)
            {
                return false;
            }
        }, new StateStartAction()
        {
            @Override
            public void onStateStart(GameWorld world)
            {
                for (Obstacle obstacle : world.getLevel().getObstacles())
                    obstacle.addAction(Actions.sequence(Actions.fadeOut(fadeDelay), Actions.removeActor()));
                for (int i = 0; i < world.getLevel().getTeams().size(); i++)
                    if (i != world.winningTeam)
                        for (Tank tank : world.getLevel().getTeams().get(i).getTanks())
                            if (!tank.equals(world.getCurrentTank()))
                                for (Image image : tank.getImages())
                                    image.addAction(Actions.sequence(Actions.fadeOut(fadeDelay), Actions.removeActor()));
                world.showLevelOverUI(new NextLevelUI(world, message));
                world.stopAllTanks();
            }
        });
    }
}
