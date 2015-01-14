package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.players.MediumComputer;
import com.wernerapps.tanks.states.AnimationState;
import com.wernerapps.tanks.states.ComputerMovingTurnState;
import com.wernerapps.tanks.states.ComputerShootingTurnState;
import com.wernerapps.tanks.states.MovingTurnState;
import com.wernerapps.tanks.states.ScrollingState;
import com.wernerapps.tanks.states.ShootingTurnState;

public class Level4 extends Level3
{
    public Level4()
    {
        this("Take turns");
    }

    public Level4(String message)
    {
        super(message);
    }

    @Override
    protected int getWidthFactor()
    {
        return 1;
    }

    @Override
    protected int getHeightFactor()
    {
        return 1;
    }

    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
        TextureRegion sandTex = AssetLoader.textureAtlas.get("sandbagBeige.png");

        for (int i = -2; i < 3; i++)
        {
            float xPos = bounds.width / 2;
            if (Math.abs(i) < 2)
                xPos -= 50 * i;
            addBag(world, sandTex, xPos, world.getHeight() / 4 + i * sandTex.getRegionWidth(), 90);
        }
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(0, world.getHeight() / 4 + 0));
        positions.add(new Vector2(-100, world.getHeight() / 4 + -100));
        positions.add(new Vector2(-100, world.getHeight() / 4 + 100));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose..."));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(bounds.width + 100, world.getHeight() / 4 + -50));
        positions.add(new Vector2(bounds.width + 100, world.getHeight() / 4 + 50));
        teams.add(new TankTeam(3, positions, new MediumComputer(10, 1), "Player wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 3;
    }

    @Override
    protected void createStates()
    {
        states.add(new ScrollingState());
        states.add(new MovingTurnState());
        states.add(new ShootingTurnState());
        states.add(new AnimationState());

        // Scroll to the computer tank
        states.add(new ScrollingState());
        states.add(new ComputerMovingTurnState());
        states.add(new ComputerShootingTurnState());
        states.add(new AnimationState());
    }
}
