package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public interface Placeable
{
    public abstract void setPosition(Vector2 newPosition);
    public abstract void rotateBy(float amount);
    public abstract void placeObject(Stage stage, int startOfNonBackground);
    public abstract void removeObject();
    public abstract Vector2 getPosition();
    public abstract float getRotation();
}