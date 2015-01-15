package com.wernerapps.tanks.levelcreator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.wernerapps.tanks.helpers.AssetLoader;

public class CreateUI implements GameUI
{
    private Image     leftArrow;
    private Actor     downArrow;
    private Actor     rightArrow;
    private Image     upArrow;
    private Rectangle leftArrowBounds;
    private Rectangle downArrowBounds;
    private Rectangle rightArrowBounds;
    private Rectangle upArrowBounds;

    public CreateUI(LevelCreatorStage stage)
    {
        leftArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        leftArrow.setOrigin(leftArrow.getWidth() / 2, leftArrow.getHeight() / 2);
        leftArrow.rotateBy(90);
        stage.addActor(leftArrow);

        downArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        downArrow.setOrigin(downArrow.getWidth() / 2, downArrow.getHeight() / 2);
        downArrow.rotateBy(180);
        stage.addActor(downArrow);

        rightArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        rightArrow.setOrigin(rightArrow.getWidth() / 2, rightArrow.getHeight() / 2);
        rightArrow.rotateBy(-90);
        stage.addActor(rightArrow);

        upArrow = new Image(AssetLoader.textureAtlas.get("arrow.png"));
        upArrow.setOrigin(upArrow.getWidth() / 2, upArrow.getHeight() / 2);
        stage.addActor(upArrow);
    }

    @Override
    public void handleTouchDown(LevelCreatorStage stage, int screenX, int screenY)
    {
        Vector2 temp = new Vector2(screenX, screenY);
        float unit = upArrow.getWidth();
        float width = stage.getWidth();
        float height = stage.getHeight();

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyDown(Keys.UP);
        else
            stage.getController().setKeyUp(Keys.UP);

        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.LEFT);
        else
            stage.getController().setKeyUp(Keys.LEFT);

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.DOWN);
        else
            stage.getController().setKeyUp(Keys.DOWN);

        if (between(width - unit, width, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyDown(Keys.RIGHT);
        else
            stage.getController().setKeyUp(Keys.RIGHT);
    }

    private boolean between(float start, float end, float test)
    {
        return test > start && test < end;
    }

    @Override
    public void handleTouchUp(LevelCreatorStage stage, int screenX, int screenY)
    {
        Vector2 temp = new Vector2(screenX, screenY);
        float unit = upArrow.getWidth();
        float width = stage.getWidth();
        float height = stage.getHeight();

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit * 2, height - unit, temp.y))
            stage.getController().setKeyUp(Keys.UP);

        if (between(width - unit * 3, width - unit * 2, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.LEFT);

        if (between(width - unit * 2, width - unit, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.DOWN);

        if (between(width - unit, width, temp.x) && between(height - unit, height, temp.y))
            stage.getController().setKeyUp(Keys.RIGHT);
    }

    @Override
    public void draw(LevelCreatorStage stage)
    {
        Vector2 temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 3, stage
                .getHeight()));
        leftArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 2, stage
                .getHeight()));
        downArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth(), stage.getHeight()));
        rightArrow.setPosition(temp.x, temp.y);

        temp = stage.screenToStageCoordinates(new Vector2(stage.getWidth() - leftArrow.getWidth() * 2, stage
                .getHeight() - upArrow.getHeight()));
        upArrow.setPosition(temp.x, temp.y);
    }
}
