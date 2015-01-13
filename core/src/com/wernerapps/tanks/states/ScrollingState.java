package com.wernerapps.tanks.states;

import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;

public class ScrollingState extends StateCondition
{
    public ScrollingState()
    {
        super(GameState.SCROLLING, new Condition()
        {
            @Override
            public boolean isDone(GameWorld world, float delta)
            {
                return world.getCameraController().scrollingDone(world.getCamera(), delta);
            }
        }, new StateStartAction()
        {
            @Override
            public void onStateStart(GameWorld world)
            {
                world.changeTeams();
                if (world.getCurrentTank() == null)
                {
                    world.getLevel().update(world, 0);
                    world.showScrollingUI();
                    System.out.println("Exiting with null tank");
                    return;
                }
                Vector2 goal = new Vector2(world.getCurrentTank().getPosition().x, world.getCurrentTank().getPosition().y);
                Vector2 camera = new Vector2(world.getCamera().position.x, world.getCamera().position.y);
                System.out.println("Goal: " + goal);
                System.out.println("Camera: " + camera);
                
                world.getCameraController().setCameraGoal(goal);
                Vector2 velocity = new Vector2(goal.x - camera.x, -(goal.y - camera.y));
                velocity.setLength2(1).scl(world.getCameraController().getCameraSpeed());
                world.getCameraController().setCameraVelocity(velocity);

                world.showScrollingUI();
            }
        });
    }

}
