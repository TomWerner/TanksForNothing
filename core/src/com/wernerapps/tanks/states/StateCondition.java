package com.wernerapps.tanks.states;

import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class StateCondition
{
    public interface Condition
    {
        public boolean isDone(GameWorld world, float delta);
    }
    public interface StateStartAction
    {
        void onStateStart(GameWorld world);
    }

    private GameState gameState;
    private Condition condition;
    private StateStartAction stateStartAction;
    
    public StateCondition(GameState gameState, Condition condition, StateStartAction stateStartAction)
    {
        this.setGameState(gameState);
        this.setCondition(condition);
        this.setStateStartAction(stateStartAction);
    }
    
    public boolean isDone(GameWorld world, float delta)
    {
        return condition.isDone(world, delta);
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }

    public Condition getCondition()
    {
        return condition;
    }

    public void setCondition(Condition condition)
    {
        this.condition = condition;
    }

    public void onStateStart(GameWorld gameWorld)
    {
        stateStartAction.onStateStart(gameWorld);
    }

    public StateStartAction getStateStartAction()
    {
        return stateStartAction;
    }

    public void setStateStartAction(StateStartAction stateStartAction)
    {
        this.stateStartAction = stateStartAction;
    }
    
    
}
