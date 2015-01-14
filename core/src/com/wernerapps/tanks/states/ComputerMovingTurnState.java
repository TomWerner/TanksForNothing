package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class ComputerMovingTurnState extends StateCondition
{
    public ComputerMovingTurnState()
    {
        super(GameState.COMP_MOVING_TURN);
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
        System.out.println("STARTING MOVING TURN");
    }

}