package com.wernerapps.tanks.players;

import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Tank.TankState;

public abstract class ComputerController extends TeamController
{
    private boolean movingGoalSet  = false;
    private boolean shootTargetSet = false;
    private int     numShots;
    private int     shotsFired;

    public ComputerController()
    {
        this(1);
    }

    public ComputerController(int numShots)
    {
        super();
        this.numShots = numShots;
    }

    @Override
    public void handleTouchDown(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
    }

    @Override
    public void handleTouchUp(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
    }

    @Override
    public void act(GameWorld world, float delta)
    {
        switch (world.getLevel().getState().getGameState())
        {
        case COMP_MOVING_TURN:
            handleMovingTurn(world);
            break;
        case COMP_SHOOTING_TURN:
            handleShootingTurn(world);
            break;
        default:
            resetController();
            break;
        }
    }

    protected void handleShootingTurn(GameWorld world)
    {
        if (!shootTargetSet)
        {
            float targetAngle = calculateShootingGoal(world);
            world.getCurrentTank().setAimingGoal(targetAngle);
            shootTargetSet = true;
        }
        else if (world.getCurrentTank().tankState.equals(TankState.SHOOTING))
        {
            if (shotsFired < numShots)
            {
                if (world.getCurrentTank().shoot(world))
                    shotsFired++;
                world.getCurrentTank().tankState = TankState.SHOOTING;
            }
            else
            {
                world.getCurrentTank().tankState = TankState.DONE;
                world.getLevel().advanceState(world);
            }
        }
    }

    protected abstract float calculateShootingGoal(GameWorld world);

    protected void handleMovingTurn(GameWorld world)
    {
        if (!movingGoalSet)
        {
            Vector2 goal = calculateMovingGoal(world);
            world.getCurrentTank().setMoveGoal(goal);
            movingGoalSet = true;
        }
        else if (movingGoalReached(world))
            world.getLevel().advanceState(world);
    }

    protected abstract boolean movingGoalReached(GameWorld world);

    protected abstract Vector2 calculateMovingGoal(GameWorld world);

    protected void resetController()
    {
        movingGoalSet = false;
        shootTargetSet = false;
        shotsFired = 0;
    }

}
