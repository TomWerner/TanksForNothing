package com.wernerapps.tanks.gameobjects;

import com.wernerapps.tanks.game.GameWorld;

public interface Updateable
{
    public boolean update(GameWorld world, float delta);
    public void destroy(GameWorld world);
}
