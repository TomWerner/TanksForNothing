package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Portal extends Obstacle
{
    public Portal(TextureRegion texture, Vector2 position)
    {
        super(texture, position, 1000);
    }

    @Override
    protected Circle createBounds()
    {
        return new Circle(position.cpy().add(getOriginX(), getOriginY()), getWidth() / 8);
    }
    
    public Circle getDoneBounds()
    {
        Circle circle = new Circle(getBounds());
        circle.radius = getWidth() / 2;
        return circle;
    }
    
    public boolean update(float delta)
    {
        rotateBy(10);
        return false;
    }
}
