package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;
import com.wernerapps.tanks.gameobjects.Background;
import com.wernerapps.tanks.gameobjects.Portal;
import com.wernerapps.tanks.gameobjects.Sandbag;
import com.wernerapps.tanks.gameobjects.Tank;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.states.LevelDoneState;

public class Level1 extends Level
{
    @Override
    protected void createOther(GameWorld world, Rectangle bounds)
    {

    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(0, 0));
        teams.add(new TankTeam(1, positions, new HumanController(), "Player wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
    }

    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
        TextureRegion sandbag = getBasicObstacleTexture();

        for (int i = -2; i < 2; i++)
        {
            Sandbag bag = new Sandbag(sandbag,
                    new Vector2(i * sandbag.getRegionWidth(), -2 * sandbag.getRegionHeight()));
            obstacles.add(bag);
            world.addActor(bag);
        }

        TextureRegion portalTexture = AssetLoader.textureAtlas.get("treeSmall.png");
        portal = new Portal(portalTexture, new Vector2(-portalTexture.getRegionWidth() / 2, -125
                - portalTexture.getRegionHeight()));
        obstacles.add(portal);
        world.addActor(portal);
    }

    @Override
    protected Rectangle createBackground(GameWorld world)
    {
        TextureRegion ground = getBackgroundTexture();
        int widthFactor = getWidthFactor();
        int heightFactor = getHeightFactor();

        int width = (int) world.getWidth() / ground.getRegionWidth();
        int height = (int) world.getHeight() / ground.getRegionHeight();

        for (int i = -(width / 2 + 1); i < width * widthFactor + (width / 2 + 1); i++)
        {
            for (int k = -(height / 2 + 1); k < height * heightFactor + (height / 2 + 2); k++)
            {
                Background image = new Background(ground);
                image.setPosition(image.getWidth() * i, image.getHeight() * k);
                world.addActor(image);
            }
        }
        return new Rectangle(0, 0, world.getWidth() * (widthFactor), world.getHeight() * (heightFactor));
    }

    protected int getWidthFactor()
    {
        return 0;
    }

    protected int getHeightFactor()
    {
        return 0;
    }

    @Override
    public void update(GameWorld world, float delta)
    {
        portal.update(delta);

        for (Tank tank : teams.get(0).getTanks())
        {
            if (Intersector.overlaps(portal.getDoneBounds(), tank.getBounds())
                    && !getState().getGameState().equals(GameState.LEVEL_DONE))
            {
                states.add(new LevelDoneState(teams.get(0).getDefeatMessage()));
                stateIndex = states.size() - 2;
                advanceState(world);
            }
        }
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 30;
    }

    protected TextureRegion getBackgroundTexture()
    {
        return AssetLoader.textureAtlas.get("dirt.png");
    }

    protected TextureRegion getBasicObstacleTexture()
    {
        return AssetLoader.textureAtlas.get("sandbagBeige.png");
    }
}
