package com.wernerapps.tanks.players;

import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class HumanController extends TeamController
{
    public HumanController()
    {
        super();
    }

    @Override
    public void handleTouchDown(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
        switch (world.getLevel().getState().getGameState())
        {
        case ANIMATIONS:
            break;
        case MOVING_TURN:
            executeMovingTurnTouchDown(world, screenX, screenY, pointer, button);
            break;
        case SCROLLING:
            break;
        case SHOOTING_TURN:
            executeShootingTurnTouchDown(world, screenX, screenY, pointer, button);
            break;
        default:
            break;
        }
    }

    @Override
    public void handleTouchUp(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
        if (world.getLevel().getState().getGameState().equals(GameState.SHOOTING_TURN))
        {
            if (screenX < world.turnLeft.getWidth() && (world.getHeight() - screenY) < world.turnLeft.getHeight())
                world.getCurrentTank().turnBarrelLeft(false);
            if ((world.getWidth() - screenX) < world.turnRight.getWidth()
                    && (world.getHeight() - screenY) < world.turnRight.getHeight())
                world.getCurrentTank().turnBarrelRight(false);
        }
    }

    @Override
    public void act(GameWorld world, float delta)
    {

    }

    private void executeShootingTurnTouchDown(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
        if (screenX < world.turnLeft.getWidth() && (world.getHeight() - screenY) < world.turnLeft.getHeight())
            world.getCurrentTank().turnBarrelLeft(true);
        if ((world.getWidth() - screenX) < world.turnRight.getWidth()
                && (world.getHeight() - screenY) < world.turnRight.getHeight())
            world.getCurrentTank().turnBarrelRight(true);
        if ((screenX > world.getWidth() / 2 - world.shootPanel.getWidth() / 2 && screenX < world.getWidth() / 2
                + world.shootPanel.getWidth() / 2)
                && (world.getHeight() - screenY) < world.turnRight.getHeight())
        {
            world.getCurrentTank().shoot(world);
            world.getLevel().advanceState(world);
        }
    }

    private void executeMovingTurnTouchDown(GameWorld world, int screenX, int screenY, int pointer, int button)
    {
        if ((world.getWidth() - screenX) < world.turnRight.getWidth()
                && (world.getHeight() - screenY) < world.turnRight.getHeight())
        {
            world.getLevel().advanceState(world);
        }
        else
        {
            Vector2 tankGoal = new Vector2(screenX + world.getCamera().position.x - world.getWidth() / 2,
                    world.getHeight() / 2 - screenY + world.getCamera().position.y);
            world.getCurrentTank().setMoveGoal(tankGoal);
            System.out.println("Clicked screen in MOVING state, setting tank goal to " + tankGoal);
        }
    }
}
