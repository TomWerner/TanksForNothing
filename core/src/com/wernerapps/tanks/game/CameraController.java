package com.wernerapps.tanks.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CameraController
{
    private Vector2 cameraGoal;
    private int     cameraSpeed;
    private Vector2 cameraVelocity;
    private float   oldLen2;

    public CameraController(int cameraSpeed)
    {
        this.cameraSpeed = cameraSpeed;
    }

    public boolean scrollingDone(Camera camera, float delta)
    {
        if (cameraGoal == null)
            return false;
        float newLen2 = new Vector2(camera.position.x, camera.position.y).sub(cameraGoal).len();
        boolean gotThere = newLen2 >= oldLen2 + getCameraSpeed() * delta / 2 || newLen2 < getCameraSpeed() * delta / 2;
//        if (gotThere)
//        {
//            System.out.println("OLD: " + oldLen2);
//            System.out.println("NEW: " + newLen2);
//            System.out.println();
//        }
        oldLen2 = Math.min(oldLen2, newLen2);
//        System.out.println(oldLen2 + ", " + newLen2);

        return (Math.abs(getCameraGoal().x - camera.position.x) <= getCameraSpeed() * delta && Math
                .abs(getCameraGoal().y - camera.position.y) <= getCameraSpeed() * delta) || gotThere;
    }

    public void scrollCamera(Camera camera, float delta)
    {
        if (!scrollingDone(camera, delta) && getCameraVelocity() != null)
        {
            Vector2 temp = new Vector2(camera.position.x, camera.position.y);
            temp.x += getCameraVelocity().x * delta;
            temp.y -= getCameraVelocity().y * delta;
            camera.position.x = temp.x;
            camera.position.y = temp.y;
        }
    }

    public void setCameraPosition(Camera camera, Vector2 position)
    {
        camera.position.x = position.x;
        camera.position.y = position.y;
        oldLen2 = Float.MAX_VALUE;
    }

    public Vector2 getCameraGoal()
    {
        return cameraGoal;
    }

    public void setCameraGoal(Vector2 cameraGoal)
    {
        this.cameraGoal = cameraGoal;
        oldLen2 = Float.MAX_VALUE;
    }

    public int getCameraSpeed()
    {
        return cameraSpeed;
    }

    public void setCameraSpeed(int cameraSpeed)
    {
        this.cameraSpeed = cameraSpeed;
    }

    public Vector2 getCameraVelocity()
    {
        return cameraVelocity;
    }

    public void setCameraVelocity(Vector2 cameraVelocity)
    {
        this.cameraVelocity = cameraVelocity;
    }

    public void boundCamera(Camera camera, Rectangle gameBounds)
    {
        camera.position.x = Math.max(camera.position.x, 0);
        camera.position.y = Math.max(camera.position.y, 0);

        camera.position.x = Math.min(camera.position.x, gameBounds.width);
        camera.position.y = Math.min(camera.position.y, gameBounds.height);
    }

}