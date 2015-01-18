package com.wernerapps.tanks.players;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Projectile;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.gameobjects.Tank.TankState;

public class MediumComputer extends ComputerController
{
    private float fudgeFactor = 0;

    public MediumComputer()
    {
        this(0, 1);
    }

    public MediumComputer(int numShots)
    {
        this(0, numShots);
    }

    public MediumComputer(float fudgeFactor, int numShots)
    {
        super(numShots);
        this.fudgeFactor = fudgeFactor;
    }

    @Override
    protected float calculateShootingGoal(GameWorld world)
    {
        long time = System.currentTimeMillis();
        // Choose an enemy tank
        ArrayList<Tank> enemies = getEnemyTanks(world);
        Pair<Tank, Integer> target = getBestTarget(world, enemies, world.getCurrentTank().getPosition());
        Tank bestTank = target.getKey();

        Vector2 difference = bestTank.getPosition().cpy().sub(world.getCurrentTank().getPosition().cpy());
        float goal = (float) (Math.toDegrees(Math.atan2(difference.y, difference.x)));
        System.out.println("Aiming took: " + (System.currentTimeMillis() - time) + " milliseconds");

        float fudge = (fudgeFactor * 2 * (float) Math.random()) - fudgeFactor;
        goal += fudge;

        return goal;
    }

    protected Pair<Tank, Integer> getBestTarget(GameWorld world, ArrayList<Tank> enemies, Vector2 position)
    {
        // Find the enemy with the best shot
        Tank bestTank = null;
        int minObstacles = Integer.MAX_VALUE;
        Vector2 pos = position.cpy();
        float stepLen = new Projectile(1, false, new Vector2(0, 0), 0, 0, 0).getHeight();

        for (Tank tank : enemies)
        {
            int obstacleCount = numObstaclesBetweenPoints(world, pos, tank.getPosition().cpy(), stepLen, stepLen * 1.5f);

            // If this shot has fewer obstacles use it
            if (obstacleCount < minObstacles)
            {
                bestTank = tank;
                minObstacles = obstacleCount;
            }
        }

        return new Pair<Tank, Integer>(bestTank, minObstacles);
    }

    protected int numObstaclesBetweenPoints(GameWorld world, Vector2 position1, Vector2 position2, float stepLength,
            float radius)
    {
        int obst = 0;

        Vector2 start = position1.cpy();
        Vector2 end = position2.cpy();
        Vector2 difference = end.cpy().sub(start);
        Vector2 step = difference.cpy().setLength(stepLength);
        float numSteps = difference.len() / step.len();

        for (int i = 1; i < numSteps; i++)
        {
            start.add(step);

            for (Obstacle obstacle : world.getLevel().getObstacles())
                if (Intersector.overlaps(obstacle.getBounds(), new Circle(start, radius * 1.25f)))
                    obst++;
        }

        return obst;
    }

    protected ArrayList<Tank> getEnemyTanks(GameWorld world)
    {
        ArrayList<Tank> result = new ArrayList<Tank>();
        for (int i = 0; i < world.getLevel().getTeams().size(); i++)
            if (i != world.currentTeam)
                result.addAll(world.getLevel().getTeams().get(i).getTanks());
        return result;
    }

    @Override
    protected boolean movingGoalReached(GameWorld world)
    {
        return world.getCurrentTank().tankState.equals(TankState.DONE);
    }

    protected int getRange(GameWorld world)
    {
        return (int) (world.getCurrentTank().getMaxSpeed() * world.getLevel().getMaxFuelSeconds());
    }

    @Override
    protected Vector2 calculateMovingGoal(GameWorld world)
    {
        long time = System.currentTimeMillis();
        ArrayList<Tank> enemies = getEnemyTanks(world);
        Vector2 ourPosition = world.getCurrentTank().getPosition().cpy();
        int range = getRange(world);
        float step = world.getCurrentTank().getImages()[0].getWidth();
        int radius = (int) (range / step);
        Vector2 bestGoal = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        int fewestObst = Integer.MAX_VALUE;
        Vector2 trialGoal;

        for (int i = -radius; i < radius; i++)
        {
            for (int k = -radius; k < radius; k++)
            {
                // Get our test position
                trialGoal = ourPosition.cpy().add(i * step, k * step);

                // Find the best target
                Pair<Tank, Integer> temp = getBestTarget(world, enemies, trialGoal);

                // Get the position of that tank
                Vector2 enemyPosition = temp.getKey().getPosition();

                boolean fewerObstacles = temp.getValue() <= fewestObst;
                boolean closerToTarget = Vector2.dst(enemyPosition.x, enemyPosition.y, trialGoal.x, trialGoal.y) < Vector2
                        .dst(enemyPosition.x, enemyPosition.y, bestGoal.x, bestGoal.y);
                boolean noObstaclesInWay = numObstaclesBetweenPoints(world, ourPosition, trialGoal, step, temp.getKey()
                        .getBounds().radius) == 0;

                if (fewerObstacles && closerToTarget && noObstaclesInWay)
                {
                    bestGoal = trialGoal.cpy();
                    fewestObst = temp.getValue();
                }
            }
        }
        System.out.println("Moving calculation took: " + (System.currentTimeMillis() - time) + " milliseconds");

        return bestGoal;
    }
}
