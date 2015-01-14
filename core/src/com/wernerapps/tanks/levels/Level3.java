package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Sandbag;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.EasyComputer;
import com.wernerapps.tanks.players.HumanController;

public class Level3 extends Level2
{
    public Level3()
    {
        this("Obstacles");
    }
    
    public Level3(String message)
    {
        super(message);
    }
    
    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
        TextureRegion texture = AssetLoader.textureAtlas.get("sandbagBeige.png");

        addBag(world, texture, 0, 0, 90);
        addBag(world, texture, 0, texture.getRegionWidth(), 90);
        addBag(world, texture, 0, -texture.getRegionWidth(), 90);
        addBag(world, texture, texture.getRegionWidth(), -1.5f * texture.getRegionWidth(), 0);
        addBag(world, texture, 2 * texture.getRegionWidth(), -1.5f * texture.getRegionWidth(), 0);
        addBag(world, texture, 3 * texture.getRegionWidth(), -1.5f * texture.getRegionWidth(), 0);
    }

    protected void addBag(GameWorld world, TextureRegion texture, float x, float y, float rotation)
    {
        Sandbag bag = new Sandbag(texture, new Vector2(x, y));
        bag.rotateBy(rotation);
        world.addActor(bag);
        obstacles.add(bag);
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(-world.getWidth() / 4, 0));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose..."));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(world.getWidth() / 4, 0));
        teams.add(new TankTeam(3, positions, new EasyComputer(0, 1), "Player wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 3;
    }
}
