package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;

public abstract class Obstacle extends Image implements Updateable, Comparable<Updateable>, Placeable
{
    public enum ObstacleState {
        ANIMATING, DONE
    }

    protected Vector2    position;
    protected float      health;
    protected float      maxHealth;
    protected Circle     bounds;
    public ObstacleState state = ObstacleState.DONE;

    public Obstacle(TextureRegion texture, Vector2 pos, float maxHealth)
    {
        super(texture);
        this.position = pos;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        setOrigin(getWidth() / 2, getHeight() / 2);

        bounds = createBounds();
        setPosition(position.x, position.y);
    }

    protected abstract Circle createBounds();

    public Circle getBounds()
    {
        return bounds;
    }

    public void damage(int damage)
    {
        health -= damage;
    }

    public float getHealth()
    {
        return health;
    }

    @Override
    public void destroy(GameWorld world)
    {
        remove();
    }

    @Override
    public boolean update(GameWorld gameWorld, float delta)
    {
        return health <= 0;
    }

    @Override
    public int compareTo(Updateable other)
    {
        return OrderFactory.getOrder(this).compareTo(OrderFactory.getOrder(other));
    }

    @Override
    public void setPosition(Vector2 newPosition)
    {
        setPosition(newPosition.x, newPosition.y);
    }

    @Override
    public void placeObject(Stage stage, int startOfNonBackground)
    {
        stage.addActor(this);
        stage.getActors().insert(startOfNonBackground, stage.getActors().removeIndex(stage.getActors().size - 1));
    }

    @Override
    public Vector2 getPosition()
    {
        return new Vector2(getX(), getY());
    }

    @Override
    public void removeObject()
    {
        remove();
    }

}
