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

public class Level5 extends Level4
{
    public Level5()
    {
        this("Outnumbered");
    }
    
    public Level5(String message)
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
        float width = sandTex.getRegionWidth();
        float height = sandTex.getRegionHeight();

        // Horizontal inner wall
        addBag(world, sandTex, 0 * width, bounds.height / 2, 0);
        addBag(world, sandTex, 3 * width, bounds.height / 2, 0);
        addBag(world, sandTex, 4 * width, bounds.height / 2, 0);

        addBag(world, sandTex, 4 * width, bounds.height / 2 - height * 1, 0);

        // Horizontal outer wall
        addBag(world, sandTex, 0 * width, bounds.height / 2 - height * 2, -45);
        addBag(world, sandTex, 3 * width, bounds.height / 2 - height * 2, 0);
        addBag(world, sandTex, 4 * width, bounds.height / 2 - height * 2, 0);

        // Vertical inner wall
        addBag(world, sandTex, 4.75f * width, bounds.height / 2 + 0.75f * width, 90);
        addBag(world, sandTex, 4.75f * width, bounds.height / 2 + 1.75f * width, 90);
        addBag(world, sandTex, 4.75f * width, bounds.height / 2 + 4.57f * width, 90);

        addBag(world, sandTex, 4.75f * width + height, bounds.height / 2 + .75f * width, 90);

        // Vertical outer wall
        addBag(world, sandTex, 4.75f * width + height * 2, bounds.height / 2 + 0.75f * width, 90);
        addBag(world, sandTex, 4.75f * width + height * 2, bounds.height / 2 + 1.75f * width, 90);
        addBag(world, sandTex, 4.75f * width + height * 2, bounds.height / 2 + 4.75f * width, 135);

        // Diagional center
        float xStart = 6;
        float yStart = -.25f;
        addBag(world, sandTex, xStart * width + height, bounds.height / 2 + yStart * width, 45);
        addBag(world, sandTex, (xStart - .75f) * width + height, bounds.height / 2 + (yStart - .75f) * width, 45);
        addBag(world, sandTex, (xStart - 1.5f) * width + height, bounds.height / 2 + (yStart - 1.5f) * width, 45);

        addBag(world, sandTex, (xStart - 1.75f) * width + height, bounds.height / 2 + (yStart) * width, 45);
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(0, bounds.height));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose...", 3));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(bounds.width, 0));
        positions.add(new Vector2(bounds.width / 4, 0));
        positions.add(new Vector2(bounds.width, bounds.height * .75f));
        teams.add(new TankTeam(3, positions, new MediumComputer(), "Player wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
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

    @Override
    public float getMaxFuelSeconds()
    {
        return 5;
    }
}
