package com.wernerapps.tanks.players;

import com.wernerapps.tanks.game.GameWorld;

public abstract class TeamController
{
    public TeamController()
    {

    }

    public abstract void handleTouchDown(GameWorld world, int screenX, int screenY, int pointer, int button);

    public abstract void handleTouchUp(GameWorld world, int screenX, int screenY, int pointer, int button);

    public abstract void act(GameWorld world, float delta);
}
