package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.game.GameWorld.GameState;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.players.EasyComputer;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.states.AnimationState;
import com.wernerapps.tanks.states.ComputerShootingTurnState;
import com.wernerapps.tanks.states.LevelDoneState;
import com.wernerapps.tanks.states.ScrollingState;

public class Level2 extends Level1
{
    public Level2()
    {
        this("Shooting practice");
    }
    
    public Level2(String message)
    {
        super(message);
    }
    
    @Override
    protected void createObstacles(GameWorld world, Rectangle bounds)
    {
    }

    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(-world.getWidth() / 4, 0));
        positions.add(new Vector2(-world.getWidth() / 4, -world.getHeight() / 4));
        positions.add(new Vector2(-world.getWidth() / 4, world.getHeight() / 4));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose..."));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(world.getWidth() / 4, 0));
        teams.add(new TankTeam(3, positions, new EasyComputer(30), "Player wins!"));

        for (TankTeam team : teams)
            team.addActors(world);
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
    protected void createStates()
    {
        super.createStates();

        // Scroll to the computer tank
        states.add(new ScrollingState());
        states.add(new ComputerShootingTurnState());
        states.add(new AnimationState());
    }

    @Override
    public float getMaxFuelSeconds()
    {
        return 3;
    }
    
    
}
