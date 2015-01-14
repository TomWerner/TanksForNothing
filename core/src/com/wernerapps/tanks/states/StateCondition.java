package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public abstract class StateCondition
{
    private GameState gameState;
    
    public StateCondition(GameState gameState)
    {
        this.setGameState(gameState);
    }
    
    public abstract boolean isDone(GameWorld world, float delta);

    public abstract void onStateStart(GameWorld world);


    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }
}
