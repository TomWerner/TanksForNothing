package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.gameobjects.UprightBarrel;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.players.MediumComputer;
import com.wernerapps.tanks.states.AnimationState;
import com.wernerapps.tanks.states.ComputerMovingTurnState;
import com.wernerapps.tanks.states.ComputerShootingTurnState;
import com.wernerapps.tanks.states.MovingTurnState;
import com.wernerapps.tanks.states.ScrollingState;
import com.wernerapps.tanks.states.ShootingTurnState;

public class Level6 extends Level4
{
    @Override
    protected int getWidthFactor()
    {
        return 0;
    }

    @Override
    protected int getHeightFactor()
    {
        return 0;
    }

    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
        TextureRegion sandTex = AssetLoader.textureAtlas.get("sandbagBeige.png");
        TextureRegion upBarTex = AssetLoader.textureAtlas.get("barrelGrey_up.png");
        float width = sandTex.getRegionWidth();
        float height = sandTex.getRegionHeight();

        addBag(world, sandTex, 0, 0, 90);
        addUprightBarrel(world, upBarTex, 50, 50, 0);
    }

    protected void addUprightBarrel(GameWorld world, TextureRegion texture, int x, int y, float rotation)
    {
        UprightBarrel barrel = new UprightBarrel(texture, new Vector2(x, y));
        barrel.rotateBy(rotation);
        obstacles.add(barrel);
        world.addActor(barrel);
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(-200, 100));
        positions.add(new Vector2(-200, 0));
        positions.add(new Vector2(-200, -100));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose..."));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(200, 0));
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
//        states.add(new ComputerMovingTurnState());
        states.add(new ComputerShootingTurnState());
        states.add(new AnimationState());
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 5;
    }
}
