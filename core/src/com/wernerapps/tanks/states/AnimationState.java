package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.gameobjects.Obstacle.ObstacleState;
import com.wernerapps.tanks.gameobjects.Tank.TankState;
import com.wernerapps.tanks.gameobjects.TankTeam;

public class AnimationState extends StateCondition
{
    public AnimationState()
    {
        super(GameState.ANIMATIONS, new Condition()
        {
            @Override
            public boolean isDone(GameWorld world, float delta)
            {
                boolean animationsDone = true;
                for (TankTeam team : world.getLevel().getTeams())
                    for (Tank tank : team.getTanks())
                        if (!tank.tankState.equals(TankState.DONE))
                            animationsDone = false;
                for (Obstacle obstacle : world.getLevel().getObstacles())
                    if (!obstacle.state.equals(ObstacleState.DONE))
                        animationsDone = false;
                boolean noProjectiles = world.getLevel().getProjectiles().size() == 0;
                boolean noAnimations = world.getLevel().getAnimations().size() == 0;
                return noProjectiles && noAnimations && animationsDone;
            }
        }, new StateStartAction()
        {
            @Override
            public void onStateStart(GameWorld world)
            {
                world.showScrollingUI();
            }
        });
    }

}