package com.wernerapps.tanks.levels;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;
import com.wernerapps.tanks.gameobjects.Background;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Sandbag;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.states.LevelDoneState;

public class FileLevel extends Level
{
    private ArrayList<Obstacle> sandbags;
    private ArrayList<Vector2>  team1Positions;
    private ArrayList<Float>    team1Rotations;
    private ArrayList<Vector2>  team2Positions;
    private ArrayList<Float>    team2Rotations;
    private String              background;
    private String              filename;

    public FileLevel(String string)
    {
        super(string);
        this.filename = string;
        System.out.println(Gdx.files.internal("levels/" + string + ".txt").reader());
        Scanner scan = new Scanner(Gdx.files.internal("levels/" + string + ".txt").reader());
        background = scan.nextLine().split(":")[1];
        String sandbag = scan.nextLine().split(":")[1];
        TextureRegion sandbagTex = AssetLoader.textureAtlas.get(sandbag);
        sandbags = new ArrayList<Obstacle>();
        team1Positions = new ArrayList<Vector2>();
        team1Rotations = new ArrayList<Float>();
        team2Positions = new ArrayList<Vector2>();
        team2Rotations = new ArrayList<Float>();
        while (scan.hasNextLine())
        {
            String[] lineParts = scan.nextLine().split(":");
            if (lineParts[0].equals("obstacle"))
            {
                String[] parts = lineParts[1].split(",");
                Sandbag bag = new Sandbag(sandbagTex, new Vector2(Float.parseFloat(parts[0]),
                        Float.parseFloat(parts[1])));
                bag.rotateBy(Float.parseFloat(parts[2]));
                sandbags.add(bag);
            }
            else if (lineParts[0].equals("team1"))
            {
                String[] parts = lineParts[1].split(",");
                team1Positions.add(new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
                team1Rotations.add(Float.parseFloat(parts[2]));
            }
            else if (lineParts[0].equals("team2"))
            {
                String[] parts = lineParts[1].split(",");
                team2Positions.add(new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
                team2Rotations.add(Float.parseFloat(parts[2]));
            }
        }
        scan.close();
    }

    @Override
    protected void updateLevel(GameWorld world, float delta)
    {
        for (TankTeam team : world.getLevel().getTeams())
        {
            if (team.getTanks().size() == 0 && !getState().getGameState().equals(GameState.LEVEL_DONE))
            {
                states.add(new LevelDoneState(team.getDefeatMessage()));
                stateIndex = states.size() - 2;
                advanceState(world);
            }
        }
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 5;
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        teams.add(new TankTeam(1, team1Positions, new HumanController(), "Player 2 Wins!"));
        teams.add(new TankTeam(3, team2Positions, new HumanController(), "Player 1 Wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
    }

    @Override
    protected void createOther(GameWorld world, Rectangle bounds)
    {
    }

    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
        for (Obstacle bag : sandbags)
        {
            world.addActor(bag);
            obstacles.add(bag);
        }
    }

    public String getFilename()
    {
        return filename;
    }

    @Override
    protected Rectangle createBackground(GameWorld world)
    {
        TextureRegion ground = AssetLoader.textureAtlas.get(background);
        int widthFactor = 1;
        int heightFactor = 1;

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
}
