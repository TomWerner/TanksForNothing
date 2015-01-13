package com.wernerapps.tanks.helpers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;

public class AnimationDrawable extends BaseDrawable
{
    public final Animation anim;

    private float stateTime = 0;

    public AnimationDrawable(Animation anim)
    {
        this.anim = anim;
        setMinWidth(anim.getKeyFrames()[0].getRegionWidth());
        setMinHeight(anim.getKeyFrames()[0].getRegionHeight());
    }

    public void act(float delta)
    {
        stateTime += delta;
    }
    
    public boolean isDone()
    {
        return anim.isAnimationFinished(stateTime);
    }

    public void reset()
    {
        stateTime = 0;
    }
    
    @Override
    public void draw(Batch batch, float x, float y, float width, float height)
    {
        batch.draw(anim.getKeyFrame(stateTime), x, y, width, height);
    }
}