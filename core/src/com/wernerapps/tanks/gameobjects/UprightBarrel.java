package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.helpers.music.SoundManager.SoundEffect;

public class UprightBarrel extends Obstacle implements Updateable
{
    public UprightBarrel(TextureRegion texture, Vector2 pos)
    {
        super(texture, pos, 1);
    }

    @Override
    protected Circle createBounds()
    {
        return new Circle(position.cpy().add(getOriginX(), getOriginY()), getWidth() / 2);
    }

    @Override
    public void destroy(GameWorld world)
    {
        super.destroy(world);

        OilSlick left = new OilSlick(position.cpy().add(getOriginX() - OilSlick.oilTexture.getRegionWidth() / 2,
                getOriginY() - OilSlick.oilTexture.getRegionHeight() / 2));
        world.addActor(left);
        world.getLevel().getObstacles().add(left);
        world.getGame().getSoundManager().play(SoundEffect.OIL_SOUND);
    }
}
