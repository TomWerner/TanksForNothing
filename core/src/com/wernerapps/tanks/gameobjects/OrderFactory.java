package com.wernerapps.tanks.gameobjects;

import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class OrderFactory
{
    public static Integer getOrder(Object object)
    {
        if (object instanceof Background)
            return 0;
        else if (object instanceof OilSlick)
            return 1;
        else if (object instanceof Obstacle)
            return 2;
        else if (object instanceof TankBody)
            return 3;
        else if (object instanceof TankBarrel)
            return 4;
        else if (object instanceof Projectile)
            return 5;
        else
            return Integer.MAX_VALUE; // Image goes to the top
    }

    public static Comparator<Actor> getComparator()
    {
        Comparator<Actor> sorter = new Comparator<Actor>()
        {
            @Override
            public int compare(Actor o1, Actor o2)
            {
                return Integer.compare(OrderFactory.getOrder(o1), OrderFactory.getOrder(o2));
            }
        };
        return sorter;
    }
}

class TankBody extends Image
{
    TankBody(TextureRegion region)
    {
        super(region);
    }
}

class TankBarrel extends Image
{
    TankBarrel(TextureRegion region)
    {
        super(region);
    }
}
