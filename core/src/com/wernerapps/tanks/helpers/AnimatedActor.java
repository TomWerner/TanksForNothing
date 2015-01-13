package com.wernerapps.tanks.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Updateable;

public class AnimatedActor extends Image implements Updateable
{
    private final AnimationDrawable drawable;

    public AnimatedActor(AnimationDrawable drawable)
    {
        super(drawable);
        this.drawable = drawable;
    }

    public boolean isDone()
    {
        return drawable.isDone();
    }
    
    @Override
    public boolean update(GameWorld world, float delta)
    {
        return isDone();
    }
    
    @Override
    public void destroy(GameWorld world)
    {
        remove();
    }

    @Override
    public void act(float delta)
    {
        drawable.act(delta);
        super.act(delta);
    }
}