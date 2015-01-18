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
    private String message;

    public LevelDoneState(final String message)
    {
        super(GameState.LEVEL_DONE);
        this.message = message;
    }

    @Override
    public boolean isDone(GameWorld world, float delta)
    {
        return false;
    }

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
        if (world.getLevelNumber() == world.getGame().getLevelManager().getLevels().size())
            world.showLevelOverUI(new NextLevelUI(world, message, true));
        else
            world.showLevelOverUI(new NextLevelUI(world, message, false));
        world.stopAllTanks();
    }

}
