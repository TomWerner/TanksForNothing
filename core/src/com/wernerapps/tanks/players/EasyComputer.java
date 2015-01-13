package com.wernerapps.tanks.players;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.gameobjects.Tank.TankState;

public class EasyComputer extends ComputerController
{
    private float fudgeFactor;

    public EasyComputer(float fudgeFactor)
    {
        this(fudgeFactor, 1);
    }
    
    public EasyComputer(float fudgeFactor, int numShots)
    {
        super(numShots);
        this.fudgeFactor = fudgeFactor;
    }

    @Override
    protected float calculateShootingGoal(GameWorld world)
    {
        // Choose an enemy tank
        ArrayList<Tank> enemies = new ArrayList<Tank>();
        for (int i = 0; i < world.getLevel().getTeams().size(); i++)
            if (i != world.currentTeam)
                enemies.addAll(world.getLevel().getTeams().get(i).getTanks());
        int choice = (int) (Math.random() * enemies.size());

        Tank tank = enemies.get(choice);
        Vector2 difference = tank.getPosition().cpy().sub(world.getCurrentTank().getPosition().cpy());
        float goal = (float) (Math.toDegrees(Math.atan2(difference.y, difference.x)));

        float fudge = (fudgeFactor * 2 * (float) Math.random()) - fudgeFactor;
        goal += fudge;

        return goal;
    }

    @Override
    protected boolean movingGoalReached(GameWorld world)
    {
        return world.getCurrentTank().tankState.equals(TankState.DONE);
    }

    @Override
    protected Vector2 calculateMovingGoal(GameWorld world)
    {
        return world.screenToStageCoordinates(new Vector2(world.getWidth() * (float) Math.random(), world.getHeight()
                * (float) Math.random()));
    }

}
