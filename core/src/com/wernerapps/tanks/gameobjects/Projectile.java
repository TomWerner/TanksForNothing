package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.helpers.AnimatedActor;
import com.wernerapps.tanks.helpers.AnimationDrawable;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.helpers.music.SoundManager.SoundEffect;

public class Projectile extends Image implements Updateable
{
    private Vector2   velocity;
    private Vector2   position;
    private Circle    boundingCircle;
    private Animation shootingAnimation;
    private float     fuel;

    public Projectile(int color, boolean bigTank, Vector2 position, float angle, float speed, float fuel)
    {
        super(AssetLoader.getBulletByName(color, bigTank));
        setOrigin(getWidth() / 2, getHeight() / 2);
        boundingCircle = new Circle(position, getHeight() / 2);

        position.x -= getOriginX();
        position.y -= getOriginY();
        this.setPosition(position);
        rotateBy(angle - 90);

        setPosition(position.x, position.y);
        float xSpeed = speed * (float) Math.cos(Math.toRadians(angle));
        float ySpeed = speed * (float) Math.sin(Math.toRadians(angle));
        velocity = new Vector2(xSpeed, ySpeed);
        shootingAnimation = AssetLoader.getShotAnimation(color);

        this.fuel = fuel;
    }

    @Override
    public boolean update(GameWorld world, float delta)
    {
        fuel -= delta;
        if (fuel <= 0)
        {
            world.getGame().getSoundManager().play(SoundEffect.TANK_HIT, .25f);
            return true;
        }

        setPosition(getPosition().add(velocity.cpy().scl(delta)));
        setPosition(getPosition().x, getPosition().y);
        boundingCircle.setPosition(getPosition().x + getOriginX(), getPosition().y + getOriginY());

        for (TankTeam team : world.getLevel().getTeams())
        {
            for (Tank tank : team.getTanks())
            {
                if (Intersector.overlaps(boundingCircle, tank.getBounds()))
                {
                    tank.damage(1);
                    world.getGame().getSoundManager().play(SoundEffect.TANK_HIT);
                    return true;
                }
            }
        }

        for (Obstacle obstacle : world.getLevel().getObstacles())
        {
            if (Intersector.overlaps(boundingCircle, obstacle.getBounds()))
            {
                obstacle.damage(1);
                world.getGame().getSoundManager().play(SoundEffect.TANK_HIT);
                return true;
            }
        }

        return false;
    }

    public Circle getBounds()
    {
        return boundingCircle;
    }

    public AnimatedActor getExplosion()
    {
        Vector2 newPos = getPosition().cpy();
        newPos.y += getOriginY();
        newPos.x -= shootingAnimation.getKeyFrames()[0].getRegionWidth() / 2;
        newPos.y -= shootingAnimation.getKeyFrames()[0].getRegionHeight() / 2;

        AnimatedActor actor = new AnimatedActor(new AnimationDrawable(shootingAnimation));
        actor.setPosition(newPos.x, newPos.y);
        return actor;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }
    
    @Override
    public void destroy(GameWorld world)
    {
        remove();
        AnimatedActor actor = getExplosion();
        world.addActor(actor);
        world.getLevel().getAnimations().add(actor);
    }
}
