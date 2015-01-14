package com.wernerapps.tanks.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wernerapps.tanks.game.GameWorld;
import com.wernerapps.tanks.helpers.AssetLoader;

public class NextLevelUI
{
    private Rectangle nextLevelBounds;
    private Rectangle replayLevelBounds;

    private String    levelComplete = "Level Complete!";
    private String    replay        = "Replay";
    private String    nextLevel     = "Next Level";

    private Vector2   levelCompletePosition;
    private Vector2   replayPosition;
    private Vector2   nextLevelPosition;

    public NextLevelUI(GameWorld world, String message)
    {
        this.levelComplete = message;
        TextBounds bounds1 = AssetLoader.fontBig.getBounds(levelComplete);
        levelCompletePosition = world.screenToStageCoordinates(new Vector2(world.getWidth() / 2 - bounds1.width / 2,
                world.getHeight() / 2 - bounds1.height * 3.5f));
        System.out.println(world.getWidth());

        TextBounds bounds2 = AssetLoader.fontBig.getBounds(replay);
        float xOffset = world.getWidth() / 2 - bounds2.width / 2;
        float yOffset = world.getHeight() / 2 - bounds2.height;
        replayPosition = world.screenToStageCoordinates(new Vector2(xOffset, yOffset));
        replayLevelBounds = new Rectangle(xOffset, yOffset, bounds2.width, bounds2.height);

        TextBounds bounds3 = AssetLoader.fontBig.getBounds(nextLevel);
        xOffset = world.getWidth() / 2 - bounds3.width / 2;
        yOffset = world.getHeight() / 2 + bounds3.height * .5f;
        nextLevelPosition = world.screenToStageCoordinates(new Vector2(xOffset, yOffset));
        nextLevelBounds = new Rectangle(xOffset, yOffset, bounds3.width, bounds3.height);
    }

    public void draw(GameWorld world)
    {
        Batch batch = world.getBatch();
        batch.begin();

        AssetLoader.fontBig.setColor(0, 0, 0, 1);

        AssetLoader.fontBig.draw(world.getBatch(), levelComplete, levelCompletePosition.x, levelCompletePosition.y);
        AssetLoader.fontBig.draw(world.getBatch(), replay, replayPosition.x, replayPosition.y);

        if (!levelComplete.equals("You lose..."))
            AssetLoader.fontBig.draw(world.getBatch(), nextLevel, nextLevelPosition.x, nextLevelPosition.y);

        batch.end();
    }

    public void checkForButtonClicked(GameWorld world, float screenX, float screenY)
    {
        if (nextLevelBounds.contains(screenX, screenY) && !levelComplete.equals("You lose..."))
        {
            world.nextLevel();
        }
        if (replayLevelBounds.contains(screenX, screenY))
        {
            world.replayLevel();
        }
    }
}
