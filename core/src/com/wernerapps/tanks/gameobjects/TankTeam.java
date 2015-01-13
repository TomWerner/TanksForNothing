package com.wernerapps.tanks.gameobjects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.players.TeamController;

public class TankTeam
{
    private ArrayList<Tank> tanks     = new ArrayList<Tank>();
    private int             tankIndex = 0;
    private TeamController  controller;
    private String          teamDefeatMessage;

    public TankTeam(int tankColor, ArrayList<Vector2> positions, TeamController controller, String teamDefeatMessage)
    {
        for (int i = 0; i < positions.size(); i++)
            tanks.add(new Tank(tankColor, positions.get(i), 1));
        this.controller = controller;
        this.teamDefeatMessage = teamDefeatMessage;
    }

    public TankTeam(int tankColor, ArrayList<Vector2> positions, TeamController controller, String teamDefeatMessage,
            int health)
    {
        for (int i = 0; i < positions.size(); i++)
            tanks.add(new Tank(tankColor, positions.get(i), health));
        this.controller = controller;
        this.teamDefeatMessage = teamDefeatMessage;
    }

    public void updateTanks(GameWorld world, float delta)
    {
        for (int i = 0; i < tanks.size();)
        {
            Tank tank = tanks.get(i);
            if (!tank.update(world, delta))
                i++;
            else
            {
                tanks.remove(i).destroy(world);

                if (i < tankIndex)
                    tankIndex--;
            }
        }
    }

    public Tank getTank()
    {
        System.out.println("Getting tank: " + tankIndex);
        if (tanks.size() == 0)
            return null;
        return tanks.get(tankIndex % tanks.size());
    }

    public void endTurn()
    {
        tankIndex++;
    }

    public void addActors(GameWorld world)
    {
        for (Tank tank : tanks)
            for (Image image : tank.getImages())
                world.addActor(image);
    }

    public ArrayList<Tank> getTanks()
    {
        return tanks;
    }

    public TeamController getController()
    {
        return controller;
    }

    public String getDefeatMessage()
    {
        return teamDefeatMessage;
    }
}
