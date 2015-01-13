package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Sandbag extends Obstacle implements Updateable
{

    public Sandbag(TextureRegion texture, Vector2 pos)
    {
        super(texture, pos, 1);
    }

    @Override
    protected Circle createBounds()
    {
        return new Circle(this.position.cpy().add(getOriginX(), getOriginY()), getWidth() * .4f);
    }
}
