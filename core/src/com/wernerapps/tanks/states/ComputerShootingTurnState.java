package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class ComputerShootingTurnState extends StateCondition
{
    public ComputerShootingTurnState()
    {
        super(GameState.COMP_SHOOTING_TURN, new Condition()
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
                // Hide everything
                world.showScrollingUI();
            }
        });
    }

}