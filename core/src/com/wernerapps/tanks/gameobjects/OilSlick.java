package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.helpers.AnimatedActor;
import com.wernerapps.tanks.helpers.AnimationDrawable;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.helpers.music.SoundManager.SoundEffect;

public class OilSlick extends Obstacle implements Updateable
{
    public static TextureRegion oilTexture = AssetLoader.textureAtlas.get("oil.png");
    private Vector2             position;
    private Circle              realBounds;
    private float               scale      = .25f;

    public OilSlick(Vector2 pos)
    {
        super(oilTexture, pos, 1);
        setPosition(pos.x, pos.y);
        this.position = pos.cpy();
        realBounds = new Circle(position.cpy().add(getOriginX(), getOriginY()), getWidth() / 4);
        setScale(scale);
        state = ObstacleState.ANIMATING;
    }

    @Override
    protected Circle createBounds()
    {
        // No collisions with it ever
        return new Circle(new Vector2(Float.MAX_VALUE, Float.MAX_VALUE), Float.MIN_VALUE);
    }

    @Override
    public boolean update(GameWorld world, float delta)
    {
        if (scale < 1)
        {
            scale += .5f * delta;
            scaleBy(.5f * delta);
        }
        else if (state.equals(ObstacleState.ANIMATING))
        {
            scale = 1;
            state = ObstacleState.DONE;
        }

        if (state.equals(ObstacleState.DONE))
        {
            for (TankTeam tankTeam : world.getLevel().getTeams())
            {
                for (Tank tank : tankTeam.getTanks())
                {
                    if (Intersector.overlaps(tank.getBounds(), realBounds))
                    {
                        world.getGame().getSoundManager().play(SoundEffect.FIRE_STARTED);
                        tank.damage(2);
                        AnimatedActor actor = getShootingAnimation();
                        world.addActor(actor);
                        world.getLevel().getAnimations().add(actor);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public AnimatedActor getShootingAnimation()
    {
        Vector2 newPos = position.cpy();
        Animation animation = AssetLoader.getFireAnimation();
        newPos.y += getOriginY();
        newPos.x += getOriginY();
        newPos.x -= animation.getKeyFrames()[0].getRegionWidth() / 2;
        newPos.y -= animation.getKeyFrames()[0].getRegionHeight() / 2;

        AnimatedActor actor = new AnimatedActor(new AnimationDrawable(animation));
        actor.setPosition(newPos.x, newPos.y);
        return actor;
    }

    @Override
    public void destroy(GameWorld world)
    {
        super.destroy(world);
    }
}
