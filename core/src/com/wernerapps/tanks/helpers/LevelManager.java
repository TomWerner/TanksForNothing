package com.wernerapps.tanks.helpers;

import java.util.ArrayList;
import java.util.List;

import com.wernerapps.tanks.levels.Level;
import com.wernerapps.tanks.levels.Level1;
import com.wernerapps.tanks.levels.Level2;
import com.wernerapps.tanks.levels.Level3;
import com.wernerapps.tanks.levels.Level4;
import com.wernerapps.tanks.levels.Level5;
import com.wernerapps.tanks.levels.Level6;

/**
 * Manages the levels.
 */
public class LevelManager
{
	private ArrayList<Level> levels = new ArrayList<Level>();

	/**
	 * Creates the level manager.
	 */
	public LevelManager()
	{
        levels.add(new Level1());
        levels.add(new Level2());
        levels.add(new Level3());
        levels.add(new Level4());
        levels.add(new Level5());
        levels.add(new Level6());
	}

	/**
	 * Retrieve all the available levels.
	 */
	public List<Level> getLevels()
	{
		return levels;
	}

    public Level getLevel(int targetLevelId)
    {
        return levels.get(targetLevelId - 1);
    }
}