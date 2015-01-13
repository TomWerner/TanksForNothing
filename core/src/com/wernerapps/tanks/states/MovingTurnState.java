package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class MovingTurnState extends StateCondition
{
    public MovingTurnState()
    {
        super(GameState.MOVING_TURN, new Condition()
        {
            @Override
            public boolean isDone(GameWorld world, float delta)
            {
                return world.getCurrentTank() == null || world.getCurrentTank().getHealth() <= 0;
            }
        }, new StateStartAction()
        {
            @Override
            public void onStateStart(GameWorld world)
            {
                world.showMovingTurnUI();
            }
        });
    }

}