package com.wernerapps.tanks.levels;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.gameobjects.TankTeam;
import com.wernerapps.tanks.players.HumanController;
import com.wernerapps.tanks.players.MediumComputer;

public class Level6 extends Level5
{
    public Level6()
    {
        this("Tables turned");
    }
    
    public Level6(String message)
    {
        super(message);
    }
    
    @Override
    protected void createTeams(GameWorld world, Rectangle bounds)
    {
        ArrayList<Vector2> positions = new ArrayList<Vector2>();
        positions.add(new Vector2(bounds.width, 0));
        positions.add(new Vector2(bounds.width / 4, 0));
        positions.add(new Vector2(bounds.width, bounds.height * .75f));
        teams.add(new TankTeam(1, positions, new HumanController(), "You lose..."));

        positions = new ArrayList<Vector2>();
        positions.add(new Vector2(0, bounds.height));
        teams.add(new TankTeam(3, positions, new MediumComputer(), "Player wins!", 3));

        for (TankTeam team : teams)
            team.addActors(world);
    }
}
