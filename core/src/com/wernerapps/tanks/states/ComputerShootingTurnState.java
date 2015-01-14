package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class ComputerShootingTurnState extends StateCondition
{
    public ComputerShootingTurnState()
    {
        super(GameState.COMP_SHOOTING_TURN);
    }

    @Override
    public boolean isDone(GameWorld world, float delta)
    {
        return false;
    }

    @Override
    public void onStateStart(GameWorld world)
    {
        // Hide everything
        world.showScrollingUI();
    }

}