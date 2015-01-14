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
import com.wernerapps.tanks.helpers.AnimatedActor;
import com.wernerapps.tanks.helpers.AnimationDrawable;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.states.LevelDoneState;

public class Level1 extends Level
{
    private AnimatedActor finishFlag;
    private Rectangle finishFlagBounds;

    public Level1()
    {
        this("Simple Steering");
    }

    public Level1(String message)
    {
        super(message);
    }

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

        finishFlag = getFlagAnimation(0, -200);
        finishFlag.setScale(1.5f);
        finishFlagBounds = new Rectangle(finishFlag.getX(), finishFlag.getY(), finishFlag.getWidth(), finishFlag.getHeight());
        world.addActor(finishFlag);
    }
    
    private AnimatedActor getFlagAnimation(float x, float y)
    {
        Vector2 newPos = new Vector2(x, y);

        AnimatedActor actor = new AnimatedActor(new AnimationDrawable(AssetLoader.getFlagAnimation()));
        actor.setPosition(newPos.x, newPos.y);
        return actor;
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
    protected void updateLevel(GameWorld world, float delta)
    {
        finishFlag.act(delta);

        for (Tank tank : teams.get(0).getTanks())
        {
            if (Intersector.overlaps(tank.getBounds(), finishFlagBounds)
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
