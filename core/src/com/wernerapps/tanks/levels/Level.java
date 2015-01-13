package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.Obstacle;
import com.wernerapps.tanks.gameobjects.Portal;
import com.wernerapps.tanks.gameobjects.Projectile;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.helpers.AnimatedActor;
import com.wernerapps.tanks.helpers.AssetLoader;
import com.wernerapps.tanks.states.AnimationState;
import com.wernerapps.tanks.states.LevelDoneState;
import com.wernerapps.tanks.states.MovingTurnState;
import com.wernerapps.tanks.states.ScrollingState;
import com.wernerapps.tanks.states.ShootingTurnState;
import com.wernerapps.tanks.states.StateCondition;

public abstract class Level
{
    protected ArrayList<Projectile>     projectiles = new ArrayList<Projectile>();
    protected ArrayList<TankTeam>       teams       = new ArrayList<TankTeam>();
    protected ArrayList<Obstacle>       obstacles   = new ArrayList<Obstacle>();
    protected ArrayList<AnimatedActor>  animations  = new ArrayList<AnimatedActor>();
    protected Portal                    portal;
    protected ArrayList<StateCondition> states      = new ArrayList<StateCondition>();
    protected int                       stateIndex  = 0;

    public Rectangle buildLevel(GameWorld world)
    {
        projectiles = new ArrayList<Projectile>();
        teams = new ArrayList<TankTeam>();
        obstacles = new ArrayList<Obstacle>();
        states = new ArrayList<StateCondition>();
        animations = new ArrayList<AnimatedActor>();
        stateIndex = 0;
        createStates();
        Rectangle bounds = createBackground(world);
        createObstacles(world, bounds);
        createTeams(world, bounds);
        createOther(world, bounds);

        createUI(world);
        return bounds;
    }

    protected abstract void createTeams(GameWorld world, Rectangle bounds);

    protected abstract Rectangle createBackground(GameWorld world);

    protected abstract void createObstacles(GameWorld world, Rectangle bounds);

    protected abstract void createOther(GameWorld world, Rectangle bounds);

    public abstract float getMaxFuelSeconds();

    public abstract void update(GameWorld gameWorld, float delta);

    public void advanceState(GameWorld world)
    {
        // System.out.println(Arrays.toString((new Throwable()).getStackTrace()));
        System.out.print("Advancing from state: " + getStates().get(stateIndex).getGameState());
        stateIndex = (stateIndex + 1) % getStates().size();
        getStates().get(stateIndex).onStateStart(world);
        System.out.print(" to " + getStates().get(stateIndex).getGameState());
        System.out.println();
    }

    public ArrayList<StateCondition> getStates()
    {
        return states;
    }

    public StateCondition getState()
    {
        return states.get(stateIndex);
    }

    public ArrayList<Projectile> getProjectiles()
    {
        return projectiles;
    }

    public ArrayList<TankTeam> getTeams()
    {
        return teams;
    }

    public ArrayList<Obstacle> getObstacles()
    {
        return obstacles;
    }

    public boolean isDone()
    {
        return getState() instanceof LevelDoneState && getState().isDone(null, 0);
    }

    protected void createStates()
    {
        states.add(new ScrollingState());
        states.add(new MovingTurnState());
        states.add(new ShootingTurnState());
        states.add(new AnimationState());
    }

    protected void createUI(GameWorld world)
    {
        world.fuelOutline = new Image(AssetLoader.textureAtlas.get("fuelOutline.png"));
        world.fuelOutline.setScale(3);
        world.fuelOutline.rotateBy(90);
        world.fuelOutline.setVisible(false);
        world.addActor(world.fuelOutline);

        world.fuel = new Image(AssetLoader.textureAtlas.get("fuel.png"));
        world.fuel.setScaleY(2.5f);
        world.fuel.setScaleX(2f);
        world.fuel.rotateBy(90);
        world.fuel.setVisible(false);
        world.addActor(world.fuel);

        world.donePanel = new Image(AssetLoader.textureAtlas.get("donePanel.png"));
        world.addActor(world.donePanel);
        world.donePanel.setVisible(false);

        world.turnLeft = new Image(AssetLoader.textureAtlas.get("turnPanel.png"));
        world.addActor(world.turnLeft);
        world.turnLeft.setVisible(false);

        world.turnRight = new Image(AssetLoader.textureAtlas.get("turnPanel.png"));
        world.addActor(world.turnRight);
        world.turnRight.setVisible(false);

        world.shootPanel = new Image(AssetLoader.textureAtlas.get("shootPanel.png"));
        world.addActor(world.shootPanel);
        world.shootPanel.setVisible(false);
    }

    public int getStartingTeam()
    {
        return 0;
    }

    public ArrayList<AnimatedActor> getAnimations()
    {
        return animations;
    }

}