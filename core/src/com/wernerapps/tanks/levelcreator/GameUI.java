package com.wernerapps.tanks.levelcreator;

public interface GameUI
{

    public abstract void handleTouchDown(LevelCreatorStage stage, int screenX, int screenY);
    public abstract void handleTouchUp(LevelCreatorStage stage, int screenX, int screenY);

    public abstract void draw(LevelCreatorStage levelCreatorStage);


}